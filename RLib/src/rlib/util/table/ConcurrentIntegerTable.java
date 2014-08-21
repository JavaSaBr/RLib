package rlib.util.table;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsynReadSynWriteLock;
import rlib.concurrent.lock.LockFactory;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.IntegerArray;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Модель конкурентной таблицы с примитивным инт числом ключем.
 *
 * @author Ronn
 */
public class ConcurrentIntegerTable<V> extends AbstractTable<IntKey, V> {

	/**
	 * Модель итератора по таблице.
	 *
	 * @author Ronn
	 */
	private final class TableIterator implements Iterator<V> {

		/** следующий entry */
		private ConcurrentIntegerTableEntry<V> next;

		/** текущий entry */
		private ConcurrentIntegerTableEntry<V> current;

		/** текущий индекс в таблице */
		private int index;

		private TableIterator() {

			final ConcurrentIntegerTableEntry<V>[] table = table();

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
		private ConcurrentIntegerTableEntry<V> nextEntry() {

			final ConcurrentIntegerTableEntry<V>[] table = table();
			final ConcurrentIntegerTableEntry<V> entry = next;

			if(entry == null) {
				throw new NoSuchElementException();
			}

			if((next = entry.getNext()) == null) {
				while(index < table.length && (next = table[index++]) == null);
			}

			current = entry;
			return entry;
		}

		@Override
		public void remove() {

			if(current == null) {
				throw new IllegalStateException();
			}

			final int key = current.getKey();
			current = null;

			removeEntryForKey(key);
		}
	}

	/** пул ячеяк */
	private final FoldablePool<ConcurrentIntegerTableEntry<V>> entryPool;
	/** блокировщик */
	private final AsynReadSynWriteLock locker;
	/** кол-во элементов в таблице */
	private final AtomicInteger size;

	/** таблица элементов */
	private volatile ConcurrentIntegerTableEntry<V>[] table;

	/** следующий размер для метода изминения размера (capacity * load factor) */
	private volatile int threshold;

	/** фактор загружеености */
	private volatile float loadFactor;

	protected ConcurrentIntegerTable() {
		this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
	}

	protected ConcurrentIntegerTable(final float loadFactor) {
		this(loadFactor, DEFAULT_INITIAL_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	protected ConcurrentIntegerTable(final float loadFactor, final int initCapacity) {
		this.loadFactor = loadFactor;
		this.threshold = (int) (initCapacity * loadFactor);
		this.size = new AtomicInteger();
		this.table = new ConcurrentIntegerTableEntry[DEFAULT_INITIAL_CAPACITY];
		this.entryPool = PoolFactory.newFoldablePool(ConcurrentIntegerTableEntry.class);
		this.locker = createLocker();
	}

	protected AsynReadSynWriteLock createLocker() {
		return LockFactory.newARSWLock();
	}

	protected ConcurrentIntegerTable(final int initCapacity) {
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
	private final void addEntry(final int hash, final int key, final V value, final int index) {

		final ConcurrentIntegerTableEntry<V>[] table = table();
		final ConcurrentIntegerTableEntry<V> entry = table[index];
		ConcurrentIntegerTableEntry<V> newEntry = entryPool.take();

		if(newEntry == null) {
			newEntry = new ConcurrentIntegerTableEntry<V>();
		}

		newEntry.set(hash, key, value, entry);
		table[index] = newEntry;

		if(size.getAndIncrement() >= threshold) {
			resize(2 * table.length);
		}
	}

	@Override
	public void apply(final Function<? super V, V> function) {
		for(ConcurrentIntegerTableEntry<V> entry : table()) {
			while(entry != null) {
				entry.setValue(function.apply(entry.getValue()));
				entry = entry.getNext();
			}
		}
	}

	@Override
	public final void clear() {

		final FoldablePool<ConcurrentIntegerTableEntry<V>> entryPool = getEntryPool();
		final ConcurrentIntegerTableEntry<V>[] table = table();

		ConcurrentIntegerTableEntry<V> next = null;

		for(ConcurrentIntegerTableEntry<V> entry : table()) {
			while(entry != null) {
				next = entry.getNext();
				entryPool.put(entry);
				entry = next;
			}
		}

		ArrayUtils.clear(table);

		size.set(0);
	}

	@Override
	public final boolean containsKey(final int key) {
		return getEntry(key) != null;
	}

	@Override
	public final boolean containsValue(final V value) {

		if(value == null) {
			throw new NullPointerException("value is null.");
		}

		for(final ConcurrentIntegerTableEntry<V> element : table()) {
			for(ConcurrentIntegerTableEntry<V> entry = element; entry != null; entry = entry.getNext()) {
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
		for(ConcurrentIntegerTableEntry<V> entry : table()) {
			while(entry != null) {
				consumer.accept(entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	@Override
	public final V get(final int key) {
		final ConcurrentIntegerTableEntry<V> entry = getEntry(key);
		return entry == null ? null : entry.getValue();
	}

	/**
	 * Получение ячейки по ключу.
	 *
	 * @param key ключ ячейки.
	 * @return ячейка.
	 */
	private final ConcurrentIntegerTableEntry<V> getEntry(final int key) {

		final int hash = hash(key);

		final ConcurrentIntegerTableEntry<V>[] table = table();

		for(ConcurrentIntegerTableEntry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.getNext()) {
			if(entry.getHash() == hash && key == entry.getKey()) {
				return entry;
			}
		}

		return null;
	}

	/**
	 * @return пул ячеяк.
	 */
	public FoldablePool<ConcurrentIntegerTableEntry<V>> getEntryPool() {
		return entryPool;
	}

	@Override
	public TableType getType() {
		return TableType.INTEGER;
	}

	@Override
	public final Iterator<V> iterator() {
		return new TableIterator();
	}

	@Override
	public IntegerArray keyIntegerArray(final IntegerArray container) {

		for(ConcurrentIntegerTableEntry<V> entry : table()) {
			while(entry != null) {
				container.add(entry.getKey());
				entry = entry.getNext();
			}
		}

		return container;
	}

	@Override
	public void moveTo(final Table<? super IntKey, ? super V> table) {

		if(isEmpty()) {
			return;
		}

		super.moveTo(table);

		for(ConcurrentIntegerTableEntry<V> entry : table()) {
			while(entry != null) {
				table.put(entry.getKey(), entry.getValue());
				entry = entry.getNext();
			}
		}
	}

	@Override
	public final V put(final int key, final V value) {

		final ConcurrentIntegerTableEntry<V>[] table = table();

		final int hash = hash(key);
		final int i = indexFor(hash, table.length);

		for(ConcurrentIntegerTableEntry<V> entry = table[i]; entry != null; entry = entry.getNext()) {
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
	public final V remove(final int key) {

		final ConcurrentIntegerTableEntry<V> old = removeEntryForKey(key);
		final V value = old == null ? null : old.getValue();

		if(old != null) {
			final FoldablePool<ConcurrentIntegerTableEntry<V>> entryPool = getEntryPool();
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
	private final ConcurrentIntegerTableEntry<V> removeEntryForKey(final int key) {

		final int hash = hash(key);

		final ConcurrentIntegerTableEntry<V>[] table = table();

		final int i = indexFor(hash, table.length);

		ConcurrentIntegerTableEntry<V> prev = table[i];
		ConcurrentIntegerTableEntry<V> entry = prev;

		while(entry != null) {

			final ConcurrentIntegerTableEntry<V> next = entry.getNext();

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
	private final void resize(final int newLength) {

		final ConcurrentIntegerTableEntry<V>[] oldTable = table();

		final int oldLength = oldTable.length;

		if(oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
			threshold = Integer.MAX_VALUE;
			return;
		}

		final ConcurrentIntegerTableEntry<V>[] newTable = new ConcurrentIntegerTableEntry[newLength];
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
	private final ConcurrentIntegerTableEntry<V>[] table() {
		return table;
	}

	@Override
	public final String toString() {

		final int size = size();

		final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append(" size = ").append(size).append(" : ");

		final ConcurrentIntegerTableEntry<V>[] table = table();

		for(int i = 0, length = table.length; i < length; i++) {

			ConcurrentIntegerTableEntry<V> entry = table[i];

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
	private final void transfer(final ConcurrentIntegerTableEntry<V>[] newTable) {

		final ConcurrentIntegerTableEntry<V>[] original = table;
		final int newCapacity = newTable.length;

		for(int j = 0, length = original.length; j < length; j++) {

			ConcurrentIntegerTableEntry<V> entry = original[j];

			if(entry == null) {
				continue;
			}

			do {

				final ConcurrentIntegerTableEntry<V> next = entry.getNext();

				final int i = indexFor(entry.getHash(), newCapacity);

				entry.setNext(newTable[i]);
				newTable[i] = entry;
				entry = next;

			} while(entry != null);
		}
	}

	@Override
	public Array<V> values(final Array<V> container) {

		for(ConcurrentIntegerTableEntry<V> entry : table()) {
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
