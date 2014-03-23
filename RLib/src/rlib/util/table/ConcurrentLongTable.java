package rlib.util.table;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.sync.AsynReadSynWriteLock;
import rlib.concurrent.sync.LockFactory;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.LongArray;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Модель конкурентной таблицы с примитивным long числом ключем.
 *
 * @author Ronn
 */
public class ConcurrentLongTable<V> extends AbstractTable<LongKey, V> {

	/**
	 * Модель ячейки в таблице.
	 *
	 * @author Ronn
	 */
	private final static class Entry<V> implements Foldable {

		/** следующая ячейка */
		private Entry<V> next;

		/** значение */
		private V value;

		/** ключ */
		private long key;

		/** хэш ключа */
		private int hash;

		@Override
		public boolean equals(Object object) {

			if(object == null || object.getClass() != Entry.class) {
				return false;
			}

			Entry<?> entry = (Entry<?>) object;

			long firstKey = getKey();
			long secondKey = entry.getKey();

			if(firstKey == secondKey) {

				Object firstValue = getValue();
				Object secondValue = entry.getValue();

				return Objects.equals(secondValue, firstValue);
			}

			return false;
		}

		@Override
		public void finalyze() {
			value = null;
			next = null;
			key = 0;
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
		public long getKey() {
			return key;
		}

		/**
		 * @return следующая ячейка.
		 */
		public Entry<V> getNext() {
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
			return (int) (key ^ (value == null ? 0 : value.hashCode()));
		}

		@Override
		public void reinit() {
			hash = 0;
		}

		public void set(int hash, long key, V value, Entry<V> next) {
			this.value = value;
			this.next = next;
			this.key = key;
			this.hash = hash;
		}

		/**
		 * @param next следующая цепочка.
		 */
		public void setNext(Entry<V> next) {
			this.next = next;
		}

		/**
		 * Установка нового значения.
		 *
		 * @param value новое значение.
		 * @return старое значение.
		 */
		public V setValue(V value) {
			V old = getValue();
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
		private Entry<V> next;
		/** текущий entry */
		private Entry<V> current;

		/** текущий индекс в таблице */
		private int index;

		private TableIterator() {

			Entry<V>[] table = table();

			if(size() > 0) {
				while(index < table.length && (next = table[index++]) == null);
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
		private Entry<V> nextEntry() {

			Entry<V>[] table = table();
			Entry<V> entry = next;

			if(entry == null) {
				throw new NoSuchElementException();
			}

			if((next = entry.getNext()) == null)
				while(index < table.length && (next = table[index++]) == null);

			current = entry;
			return entry;
		}

		@Override
		public void remove() {

			if(current == null) {
				throw new IllegalStateException();
			}

			long key = current.getKey();
			current = null;

			removeEntryForKey(key);
		}
	}

	/** пул ячеяк */
	private final FoldablePool<Entry<V>> entryPool;
	/** блокировщик */
	private final AsynReadSynWriteLock locker;
	/** кол-во элементов в таблице */
	private final AtomicInteger size;

	/** таблица элементов */
	private volatile Entry<V>[] table;

	/** следующий размер для метода изминения размера (capacity * load factor) */
	private volatile int threshold;
	/** фактор загружеености */
	private volatile float loadFactor;

	protected ConcurrentLongTable() {
		this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
	}

	protected ConcurrentLongTable(float loadFactor) {
		this(loadFactor, DEFAULT_INITIAL_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	protected ConcurrentLongTable(float loadFactor, int initCapacity) {
		this.loadFactor = loadFactor;
		this.threshold = (int) (initCapacity * loadFactor);
		this.size = new AtomicInteger();
		this.table = new Entry[DEFAULT_INITIAL_CAPACITY];
		this.entryPool = PoolFactory.newFoldablePool(Entry.class);
		this.locker = LockFactory.newARSWLock();
	}

	protected ConcurrentLongTable(int initCapacity) {
		this(DEFAULT_LOAD_FACTOR, initCapacity);
	}

	/**
	 * Добавляет новую ячейку в таблицу.
	 *
	 * @param hash хэш значение.
	 * @param key значение ключа.
	 * @param value значение по ключу.
	 * @param index индекс ячейки.
	 */
	private final void addEntry(int hash, long key, V value, int index) {

		Entry<V>[] table = table();
		Entry<V> entry = table[index];
		Entry<V> newEntry = getEntryPool().take();

		if(newEntry == null) {
			newEntry = new Entry<V>();
		}

		newEntry.set(hash, key, value, entry);

		table[index] = newEntry;

		if(size.getAndIncrement() >= threshold) {
			resize(2 * table.length);
		}
	}

	@Override
	public void apply(Function<? super V, V> function) {
		for(Entry<V> entry : table()) {
			while(entry != null) {
				entry.setValue(function.apply(entry.getValue()));
				entry = entry.getNext();
			}
		}
	}

	@Override
	public final void clear() {

		FoldablePool<Entry<V>> entryPool = getEntryPool();

		Entry<V>[] table = table();
		Entry<V> next = null;

		for(Entry<V> entry : table()) {
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
	public boolean containsKey(int key) {
		return containsKey((long) key);
	}

	@Override
	public final boolean containsKey(long key) {
		return getEntry(key) != null;
	}

	@Override
	public final boolean containsValue(V value) {

		if(value == null) {
			throw new NullPointerException("value is null.");
		}

		for(Entry<V> element : table()) {
			for(Entry<V> entry = element; entry != null; entry = entry.getNext()) {
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
	public void forEach(Consumer<? super V> consumer) {
		for(Entry<V> entry : table()) {
			while(entry != null) {
				consumer.accept(entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	@Override
	public V get(int key) {
		return get((long) key);
	}

	@Override
	public final V get(long key) {
		Entry<V> entry = getEntry(key);
		return entry == null ? null : entry.getValue();
	}

	/**
	 * Получение ячейки по ключу.
	 *
	 * @param key ключ ячейки.
	 * @return ячейка.
	 */
	private final Entry<V> getEntry(long key) {

		int hash = hash(key);

		Entry<V>[] table = table();

		for(Entry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.getNext()) {
			if(entry.getHash() == hash && key == entry.getKey()) {
				return entry;
			}
		}

		return null;
	}

	/**
	 * @return пул ячеяк.
	 */
	private FoldablePool<Entry<V>> getEntryPool() {
		return entryPool;
	}

	@Override
	public TableType getType() {
		return TableType.LONG;
	}

	@Override
	public final Iterator<V> iterator() {
		return new TableIterator();
	}

	@Override
	public LongArray keyLongArray(LongArray container) {

		for(Entry<V> entry : table()) {
			while(entry != null) {
				container.add(entry.getKey());
				entry = entry.getNext();
			}
		}

		return container;
	}

	@Override
	public void moveTo(Table<? super LongKey, ? super V> table) {

		if(isEmpty()) {
			return;
		}

		super.moveTo(table);

		for(Entry<V> entry : table()) {
			while(entry != null) {
				table.put(entry.getKey(), entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	@Override
	public V put(int key, V value) {
		return put((long) key, value);
	}

	@Override
	public final V put(long key, V value) {

		int hash = hash(key);

		Entry<V>[] table = table();

		int i = indexFor(hash, table.length);

		for(Entry<V> entry = table[i]; entry != null; entry = entry.getNext()) {
			if(entry.getHash() == hash && key == entry.getKey()) {
				return entry.setValue(value);
			}
		}

		addEntry(hash, key, value, i);

		return null;
	}

	@Override
	public void readLock() {
		locker.asynLock();
	}

	@Override
	public void readUnlock() {
		locker.asynUnlock();
	}

	@Override
	public V remove(int key) {
		return remove((long) key);
	}

	@Override
	public final V remove(long key) {

		Entry<V> old = removeEntryForKey(key);
		V value = old == null ? null : old.getValue();

		if(old != null) {
			FoldablePool<Entry<V>> entryPool = getEntryPool();
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
	private final Entry<V> removeEntryForKey(long key) {

		int hash = hash(key);

		Entry<V>[] table = table();

		int i = indexFor(hash, table.length);

		Entry<V> prev = table[i];
		Entry<V> entry = prev;

		while(entry != null) {

			Entry<V> next = entry.getNext();

			if(entry.getHash() == hash && key == entry.getKey()) {

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
	private final void resize(int newLength) {

		Entry<V>[] oldTable = table();
		int oldLength = oldTable.length;

		if(oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
			threshold = Integer.MAX_VALUE;
			return;
		}

		Entry<V>[] newTable = new Entry[newLength];
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
	private final Entry<V>[] table() {
		return table;
	}

	@Override
	public final String toString() {

		int size = size();

		StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append(" size = ").append(size).append(" : ");

		Entry<V>[] table = table();

		for(int i = 0, length = table.length; i < length; i++) {

			Entry<V> entry = table[i];

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
	private final void transfer(Entry<V>[] newTable) {

		Entry<V>[] original = table;
		int newCapacity = newTable.length;

		for(int j = 0, length = original.length; j < length; j++) {

			Entry<V> entry = original[j];

			if(entry == null) {
				continue;
			}

			do {

				Entry<V> next = entry.getNext();

				int i = indexFor(entry.getHash(), newCapacity);

				entry.setNext(newTable[i]);
				newTable[i] = entry;
				entry = next;

			} while(entry != null);
		}
	}

	@Override
	public Array<V> values(Array<V> container) {

		for(Entry<V> entry : table()) {
			while(entry != null) {
				container.add(entry.getValue());
				entry = entry.getNext();
			}
		}

		return container;
	}

	@Override
	public void writeLock() {
		locker.synLock();
	}

	@Override
	public void writeUnlock() {
		locker.synUnlock();
	}
}
