package rlib.util.table;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsynReadSynWriteLock;
import rlib.concurrent.lock.LockFactory;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Реализация таблицы с возможностью потокобезопасно асинхронно читать и
 * синхронно записывать.
 *
 * @author Ronn
 */
public class ConcurrentObjectTable<K, V> extends AbstractTable<K, V> {

	/**
	 * Модель ячейки в таблице.
	 *
	 * @author Ronn
	 */
	private final static class Entry<K, V> implements Foldable {

		/** следующая ячейка */
		private Entry<K, V> next;

		/** ключ */
		private K key;
		/** значение */
		private V value;

		/** хэш ключа */
		private int hash;

		@Override
		public boolean equals(final Object object) {

			if(object == null || object.getClass() != Entry.class) {
				return false;
			}

			final Entry<?, ?> entry = (Entry<?, ?>) object;

			final Object firstKey = getKey();
			final Object secondKey = entry.getKey();

			if(Objects.equals(firstKey, secondKey)) {

				final Object firstValue = getValue();
				final Object secondValue = entry.getValue();

				return Objects.equals(firstValue, secondValue);
			}

			return false;
		}

		@Override
		public void finalyze() {
			key = null;
			value = null;
			next = null;
			hash = 0;
		}

		/**
		 * @return хэш ячейки.
		 */
		public int getHash() {
			return hash;
		}

		/**
		 * @return ключ ячейки.
		 */
		public K getKey() {
			return key;
		}

		/**
		 * @return следующая ячейка.
		 */
		public Entry<K, V> getNext() {
			return next;
		}

		/**
		 * @return значение ячейки.
		 */
		public V getValue() {
			return value;
		}

		@Override
		public final int hashCode() {
			return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
		}

		@Override
		public void reinit() {
			hash = 0;
		}

		public void set(final int hash, final K key, final V value, final Entry<K, V> next) {
			this.value = value;
			this.next = next;
			this.key = key;
			this.hash = hash;
		}

		/**
		 * @param next следующая цепочка.
		 */
		public void setNext(final Entry<K, V> next) {
			this.next = next;
		}

		/**
		 * Установка нового значения.
		 *
		 * @param value новое значение.
		 * @return старое значение.
		 */
		public V setValue(final V value) {
			final V old = getValue();
			this.value = value;
			return old;
		}

		@Override
		public final String toString() {
			return "Entry : " + key + " = " + value;
		}
	}

	/**
	 * Модель итератора по таблице.
	 *
	 * @author Ronn
	 */
	private final class TableIterator implements Iterator<V> {

		/** следующий entry */
		private Entry<K, V> next;
		/** текущий entry */
		private Entry<K, V> current;

		/** текущий индекс в таблице */
		private int index;

		private TableIterator() {

			final Entry<K, V>[] table = table();

			if(size.get() > 0) {
				while(index < table.length && (next = table[index++]) == null) {
					;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public V next() {
			return nextEntry().getValue();
		}

		/**
		 * @return следующая занятая ячейка.
		 */
		private Entry<K, V> nextEntry() {

			final Entry<K, V>[] table = table();
			final Entry<K, V> entry = next;

			if(entry == null) {
				throw new NoSuchElementException();
			}

			if((next = entry.getNext()) == null) {
				while(index < table.length && (next = table[index++]) == null) {
					;
				}
			}

			current = entry;
			return entry;
		}

		@Override
		public void remove() {

			if(current == null) {
				throw new IllegalStateException();
			}

			final K key = current.getKey();
			current = null;

			removeEntryForKey(key);
		}
	}

	/** пул ячеяк */
	private final FoldablePool<Entry<K, V>> entryPool;
	/** блокировщики */
	private final AsynReadSynWriteLock locker;
	/** кол-во элементов в таблице */
	private final AtomicInteger size;

	/** таблица элементов */
	private volatile Entry<K, V>[] table;

	/** фактор агруженности таблицы */
	private volatile float loadFactor;
	/** размер таблицы, при котором произойдет ресайз */
	private volatile int threshold;

	protected ConcurrentObjectTable() {
		this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
	}

	protected ConcurrentObjectTable(final float loadFactor) {
		this(loadFactor, DEFAULT_INITIAL_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	protected ConcurrentObjectTable(final float loadFactor, final int initCapacity) {
		this.loadFactor = loadFactor;
		this.threshold = (int) (initCapacity * loadFactor);
		this.size = new AtomicInteger();
		this.table = new Entry[DEFAULT_INITIAL_CAPACITY];
		this.entryPool = PoolFactory.newFoldablePool(Entry.class);
		this.locker = LockFactory.newARSWLock();
	}

	protected ConcurrentObjectTable(final int initCapacity) {
		this(DEFAULT_LOAD_FACTOR, initCapacity);
	}

	@Override
	public void accept(final BiConsumer<? super K, ? super V> consumer) {
		for(Entry<K, V> entry : table()) {
			while(entry != null) {
				consumer.accept(entry.getKey(), entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	/**
	 * Добавляет новую ячейку в таблицу.
	 *
	 * @param hash хэш значение.
	 * @param key значение ключа.
	 * @param value значение по ключу.
	 * @param index индекс ячейки.
	 */
	private final void addEntry(final int hash, final K key, final V value, final int index) {

		final FoldablePool<Entry<K, V>> entryPool = getEntryPool();

		final Entry<K, V>[] table = table();
		final Entry<K, V> entry = table[index];
		Entry<K, V> newEntry = entryPool.take();

		if(newEntry == null) {
			newEntry = new Entry<K, V>();
		}

		newEntry.set(hash, key, value, entry);
		table[index] = newEntry;

		if(size.getAndIncrement() >= threshold) {
			resize(2 * table.length);
		}
	}

	@Override
	public void apply(final Function<? super V, V> function) {
		for(Entry<K, V> entry : table()) {
			while(entry != null) {
				entry.setValue(function.apply(entry.getValue()));
				entry = entry.getNext();
			}
		}
	}

	@Override
	public final void clear() {

		final FoldablePool<Entry<K, V>> entryPool = getEntryPool();

		final Entry<K, V>[] table = table();
		Entry<K, V> next = null;

		for(Entry<K, V> entry : table) {
			while(entry != null) {
				next = entry.getNext();
				entryPool.put(entry);
				entry = next;
			}
		}

		ArrayUtils.clear(table);

		size.getAndSet(0);
	}

	@Override
	public final boolean containsKey(final K key) {
		return getEntry(key) != null;
	}

	@Override
	public final boolean containsValue(final V value) {

		if(value == null) {
			throw new NullPointerException("value is null.");
		}

		for(final Entry<K, V> element : table()) {
			for(Entry<K, V> entry = element; entry != null; entry = entry.getNext()) {
				if(value.equals(entry.getValue())) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public final void finalyze() {
		if(size() > 0) {
			clear();
		}
	}

	@Override
	public void forEach(final Consumer<? super V> consumer) {
		for(Entry<K, V> entry : table()) {
			while(entry != null) {
				consumer.accept(entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	@Override
	public final V get(final K key) {

		if(key == null) {
			throw new NullPointerException("key is null.");
		}

		final Entry<K, V> entry = getEntry(key);
		return entry == null ? null : entry.getValue();
	}

	/**
	 * Получение ячейки по ключу.
	 *
	 * @param key ключ ячейки.
	 * @return ячейка.
	 */
	private final Entry<K, V> getEntry(final K key) {

		final Entry<K, V>[] table = table();

		final int hash = hash(key.hashCode());

		for(Entry<K, V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.getNext()) {
			if(entry.getHash() == hash && key.equals(entry.getKey())) {
				return entry;
			}
		}

		return null;
	}

	/**
	 * @return пул ячеяк.
	 */
	private FoldablePool<Entry<K, V>> getEntryPool() {
		return entryPool;
	}

	@Override
	public TableType getType() {
		return TableType.OBJECT;
	}

	@Override
	public final Iterator<V> iterator() {
		return new TableIterator();
	}

	@Override
	public final Array<K> keyArray(final Array<K> container) {

		for(Entry<K, V> entry : table()) {
			while(entry != null) {
				container.add(entry.getKey());
				entry = entry.getNext();
			}
		}

		return container;
	}

	@Override
	public void moveTo(final Table<? super K, ? super V> table) {

		if(isEmpty()) {
			return;
		}

		super.moveTo(table);

		for(Entry<K, V> entry : table()) {
			while(entry != null) {
				table.put(entry.getKey(), entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	@Override
	public final V put(final K key, final V value) {

		if(key == null) {
			throw new NullPointerException("key is null.");
		}

		final Entry<K, V>[] table = table();

		final int hash = hash(key.hashCode());
		final int i = indexFor(hash, table.length);

		for(Entry<K, V> entry = table[i]; entry != null; entry = entry.getNext()) {
			if(entry.getHash() == hash && key.equals(entry.getKey())) {
				return entry.setValue(value);
			}
		}

		addEntry(hash, key, value, i);

		return null;
	}

	@Override
	public final void readLock() {
		locker.asynLock();
	}

	@Override
	public final void readUnlock() {
		locker.asynUnlock();
	}

	@Override
	public final V remove(final K key) {

		if(key == null) {
			throw new NullPointerException("key is null.");
		}

		final Entry<K, V> old = removeEntryForKey(key);
		final V value = old == null ? null : old.getValue();

		if(old != null) {
			final FoldablePool<Entry<K, V>> entryPool = getEntryPool();
			entryPool.put(old);
		}

		return value;
	}

	/**
	 * Удаление значения из ячейки по указанному ключу.
	 *
	 * @param key ключ ячейки.
	 * @return удаленная ячейка.
	 */
	private final Entry<K, V> removeEntryForKey(final K key) {

		final int hash = hash(key.hashCode());

		final Entry<K, V>[] table = table();

		final int i = indexFor(hash, table.length);

		Entry<K, V> prev = table[i];
		Entry<K, V> entry = prev;

		while(entry != null) {

			final Entry<K, V> next = entry.getNext();

			if(entry.getHash() == hash && key.equals(entry.getKey())) {

				size.decrementAndGet();

				if(prev == entry) {
					table[i] = next;
				} else {
					prev.setNext(next);
				}

				return entry;
			}

			prev = entry;
			entry = next;
		}

		return entry;
	}

	/**
	 * Перестройка таблицы под новый размер.
	 *
	 * @param newLength новый размер.
	 */
	@SuppressWarnings("unchecked")
	private final void resize(final int newLength) {

		final Entry<K, V>[] oldTable = table();

		final int oldLength = oldTable.length;

		if(oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
			threshold = Integer.MAX_VALUE;
			return;
		}
		final Entry<K, V>[] newTable = new Entry[newLength];
		transfer(newTable);

		this.table = newTable;
		this.threshold = (int) (newLength * loadFactor);
	}

	@Override
	public final int size() {
		return size.get();
	}

	/**
	 * @return массив ячеяк.
	 */
	private final Entry<K, V>[] table() {
		return table;
	}

	@Override
	public final String toString() {

		final int size = size();

		final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append(" size = ").append(size).append(" : ");

		final Entry<K, V>[] table = table();

		for(int i = 0, length = table.length; i < length; i++) {

			Entry<K, V> entry = table[i];

			while(entry != null) {

				builder.append("[").append(entry.getKey()).append(" - ").append(entry.getValue()).append("]");
				builder.append("\n");

				entry = entry.getNext();
			}
		}

		if(size > 0) {
			builder.replace(builder.length() - 1, builder.length(), ".");
		}

		return builder.toString();
	}

	/**
	 * Перенос всех записей из старой таблице в новую.
	 *
	 * @param newTable новая таблица.
	 */
	private final void transfer(final Entry<K, V>[] newTable) {

		final Entry<K, V>[] original = table;

		final int newCapacity = newTable.length;

		for(int j = 0, length = original.length; j < length; j++) {

			Entry<K, V> entry = original[j];

			if(entry == null) {
				continue;
			}

			do {

				final Entry<K, V> next = entry.getNext();

				final int i = indexFor(entry.getHash(), newCapacity);

				entry.setNext(newTable[i]);
				newTable[i] = entry;
				entry = next;

			} while(entry != null);
		}
	}

	@Override
	public Array<V> values(final Array<V> container) {

		for(Entry<K, V> entry : table()) {
			while(entry != null) {
				container.add(entry.getValue());
				entry = entry.getNext();
			}
		}

		return container;
	}

	@Override
	public final void writeLock() {
		locker.synLock();
	}

	@Override
	public final void writeUnlock() {
		locker.synUnlock();
	}
}
