package rlib.util.table;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import rlib.util.array.Array;
import rlib.util.array.IntegerArray;
import rlib.util.array.LongArray;


/**
 * Таблица основаная на хэш ключе.
 * Не использовать в конкурентных местах.
 *
 * @author Ronn
 * @created 27.02.2012
 */
public final class FastTable<K, V> implements Table<K, V>
{
	/**
	 * Модель ячейки в таблице.
	 *
	 * @author Ronn
	 */
	private final static class Entry<V>
	{
		/** значение */
		private V value;

		/** следующий entry */
		private Entry<V> next;

		/** ключ */
		private final int key;
		/** хэш ключа */
		private final int hash;

		/**
		 * @param key значение ключа.
		 * @param value значение.
		 * @param next следующая ячейка.
		 * @param hash хэш ячейки.
		 */
		private Entry(int key, V value, Entry<V> next, int hash)
		{
			super();

			this.key = key;
			this.value = value;
			this.next = next;
			this.hash = hash;
		}

		@Override
		public boolean equals(Object object)
		{
			if(object.getClass() != Entry.class)
				return false;

			Entry<?> entry = (Entry<?>) object;

			if(key == entry.key && value == entry.value)
				return true;

			return false;
		}

		@Override
		public int hashCode()
		{
			return key ^ (value == null ? 0 : value.hashCode());
		}

		@Override
		public final String toString()
		{
			return key + " = " + value;
		}
	}

	/**
	 * Модель итератора по таблице.
	 *
	 * @author Ronn
	 */
	private final class TableIterator implements Iterator<V>
	{
		/** следующий entry */
		private Entry<V> next;

		/** текущий индекс в таблице */
		private int index;

		/** текущий entry */
		private Entry<V> current;

		private TableIterator()
		{
			if(size > 0)
				while(index < table.length && (next = table[index++]) == null);
		}

		@Override
		public boolean hasNext()
		{
			return next != null;
		}

		@Override
		public V next()
		{
			return nextEntry().value;
		}

		/**
		 * @return следующая занятая ячейка.
		 */
		private Entry<V> nextEntry()
		{
			Entry<V> entry = next;

			if(entry == null)
				throw new NoSuchElementException();

			if((next = entry.next) == null)
				while(index < table.length && (next = table[index++]) == null);

			current = entry;

			return entry;
		}

		@Override
		public void remove()
		{
			if(current == null)
				throw new IllegalStateException();

			int key = current.key;
			current = null;

			removeEntryForKey(key);
		}
	}

	/**
	 * Детализированный рассчет хэша.
	 *
	 * @param hash начальный хэш.
	 * @return новый хэш.
	 */
	private final static int hash(int hash)
	{
		hash ^= hash >>> 20 ^ hash >>> 12;
		return hash ^ hash >>> 7 ^ hash >>> 4;
	}

	/**
	 * Определние индекса ячейки по хэш коду.
	 *
	 * @return индекс ячейки.
	 */
	private final static int indexFor(int hash, int length)
	{
		return hash & length - 1;
	}

	/** следующий размер для метода изминения размера (capacity * load factor) */
	private int threshold;

	/** The load factor for the hash table. */
	private float loadFactor;

	/** кол-во элементов в таблице */
	private int size;

	/** таблица елементов */
	private Entry<V>[] table;

	@SuppressWarnings("unchecked")
	protected FastTable()
	{
		loadFactor = DEFAULT_LOAD_FACTOR;

		threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);

		size = 0;

		table = new Entry[DEFAULT_INITIAL_CAPACITY];
	}

	/**
	 * Добавляет новую ячейку в таблицу.
	 *
	 * @param hash хэш значение.
	 * @param key значение ключа.
	 * @param value значение по ключу.
	 * @param index индекс ячейки.
	 */
	private void addEntry(int hash, int key, V value, int index)
	{
		Entry<V> entry = table[index];

		table[index] = new Entry<V>(key, value, entry, hash);

		if(size++ >= threshold)
			resize(2 * table.length);
	}

	@Override
	public void apply(FuncKeyValue<K, V> func)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public void apply(FuncValue<V> func)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public void clear()
	{
		for(int i = 0, length = table.length; i < length; i++)
			table[i] = null;

		size = 0;
	}

	@Override
	public boolean containsKey(int key)
	{
		return getEntry(key) != null;
	}

	@Override
	public boolean containsKey(K key)
	{
		return containsKey(key == null? 0 : key.hashCode());
	}

	@Override
	public boolean containsKey(long key)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public boolean containsValue(V value)
	{
		for(int i = 0, length = table.length; i < length; i++)
		{
			Entry<V> element = table[i];

			for(Entry<V> entry = element; entry != null; entry = entry.next)
				if(value.equals(entry.value))
					return true;
		}

		return false;
	}

	@Override
	public void finalyze()
	{
		if(size > 0)
			clear();
	}

	@Override
	public V get(int key)
	{
		int hash = hash(key);

		for(Entry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.next)
			if(entry.hash == hash && entry.key == key)
				return entry.value;

		return null;
	}

	@Override
	public V get(K key)
	{
		return get(key == null? 0 : key.hashCode());
	}

	@Override
	public V get(long key)
	{
		throw new IllegalArgumentException("not supported.");
	}

	/**
	 * Получение ячейки по ключу.
	 *
	 * @param key ключ ячейки.
	 * @return ячейка.
	 */
	private Entry<V> getEntry(int key)
	{
		int hash = hash(key);

		for(Entry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.next)
			if(entry.key == key)
				return entry;

		return null;
	}

	@Override
	public TableType getType()
	{
		return TableType.DEPRECATED;
	}

	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	@Override
	public final Iterator<V> iterator()
	{
		return new TableIterator();
	}

	@Override
	public Array<K> keyArray(Array<K> container)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public IntegerArray keyIntegerArray(IntegerArray container)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public LongArray keyLongArray(LongArray container)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public void moveTo(Table<K, V> table)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public V put(int key, V value)
	{
		int hash = hash(key);

		int i = indexFor(hash, table.length);

		for(Entry<V> entry = table[i]; entry != null; entry = entry.next)
		{
			if(entry.key == key)
			{
				V oldValue = entry.value;

				entry.value = value;

				return oldValue;
			}
		}

		addEntry(hash, key, value, i);

		return null;
	}

	@Override
	public V put(K key, V value)
	{
		return put(key == null? 0 : key.hashCode(), value);
	}

	@Override
	public V put(long key, V value)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public void put(Table<K, V> table)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public void readLock(){}

	@Override
	public void readUnlock(){}

	@Override
	public void reinit(){}

	@Override
	public V remove(int key)
	{
		Entry<V> old = removeEntryForKey(key);

		return old == null ? null : old.value;
	}

	@Override
	public V remove(K key)
	{
		return remove(key == null? 0 : key.hashCode());
	}

	@Override
	public V remove(long key)
	{
		throw new IllegalArgumentException("not supported.");
	}

	/**
	 * Удаление значения из ячейки по указанному ключу.
	 *
	 * @param key ключ ячейки.
	 * @return удаленная ячейка.
	 */
	private Entry<V> removeEntryForKey(int key)
	{
		int hash = hash(key);

		int i = indexFor(hash, table.length);

		Entry<V> prev = table[i];
		Entry<V> entry = prev;

		while(entry != null)
		{
			Entry<V> next = entry.next;

			if(entry.hash == hash && entry.key == key)
			{
				size--;

				if(prev == entry)
					table[i] = next;
				else
					prev.next = next;

				return entry;
			}

			prev = entry;
			entry = next;
		}

		return entry;
	}

	/**
	 * Изминение размера таблицы.
	 *
	 * @param newLength новый размер.
	 */
	@SuppressWarnings("unchecked")
	private void resize(int newLength)
	{
		Entry<V>[] oldTable = table;

		int oldLength = oldTable.length;

		if(oldLength == MAXIMUM_CAPACITY)
		{
			threshold = Integer.MAX_VALUE;
			return;
		}

		Entry<V>[] newTable = new Entry[newLength];

		transfer(newTable);

		table = newTable;

		threshold = (int) (newLength * loadFactor);
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public String toString()
	{
		return "FastTable  size = " + size + ", " + (table != null ? "table = " + Arrays.toString(table) : "");
	}

	/**
	 * Перенос всех записей из старой таблице в новую
	 *
	 * @param newTable
	 */
	private final void transfer(Entry<V>[] newTable)
	{
		Entry<V>[] original = table;

		int newCapacity = newTable.length;

		for(int j = 0, length = original.length; j < length; j++)
		{
			Entry<V> entry = original[j];

			if(entry != null)
			{
				original[j] = null;

				do
				{
					Entry<V> next = entry.next;

					int i = indexFor(entry.hash, newCapacity);

					entry.next = newTable[i];

					newTable[i] = entry;

					entry = next;
				}
				while(entry != null);
			}
		}
	}

	@Override
	public Array<V> values(Array<V> container)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public void writeLock(){}

	@Override
	public void writeUnlock(){}
}