package rlib.util.table;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import rlib.concurrent.Locks;
import rlib.util.array.Array;
import rlib.util.array.Arrays;
import rlib.util.array.IntegerArray;
import rlib.util.array.LongArray;


/**
 * Таблица основаная на хэш ключе.
 * Использовать в конкурентных местах.
 *
 * @author Ronn
 * @created 27.02.2012
 */
public final class ConcurrentTable<K, V> implements Table<K, V>
{
	/**
	 * Модель ячейки в таблице.
	 *
	 * @author Ronn
	 */
	private final static class Entry<V>
	{
		/** значение */
		private volatile V value;

		/** следующий entry */
		private volatile Entry<V> next;

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

			// получаем ячейку
			Entry<?> entry = (Entry<?>) object;

			// проверяем на квивалентность
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
		public String toString()
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
			// если есть элементы в таблице
			if(size > 0)
			{
				readLock.lock();
				try
				{
					// находим ближайшую ячейку
					while(index < table.length && (next = table[index++]) == null);
				}
				finally
				{
					readLock.unlock();
				}
			}
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
			// запоминаем след. ячейку
			Entry<V> entry = next;

			// если такой нет, выкидываем эксепшен
			if(entry == null)
				throw new NoSuchElementException();

			readLock.lock();
			try
			{
				// ищем следующую
				if((next = entry.next) == null)
					while(index < table.length && (next = table[index++]) == null);
			}
			finally
			{
				readLock.unlock();
			}

			// запоминаем текущую
			current = entry;

			return entry;
		}

		@Override
		public void remove()
		{
			if(current == null)
				throw new IllegalStateException();

			// получаем ключ
			int key = current.key;

			// зануляем текущуюю
			current = null;

			// удаляем значение по ключу
			removeEntryForKey(key);
		}
	}

	/** блокировщики */
	private final Lock readLock;
	private final Lock writeLock;

	/** таблица елементов */
	private volatile Entry<V>[] table;

	/** размер таблицы, при котором произойдет ресайз */
	private volatile int threshold;

	/** кол-во элементов в таблице */
	private volatile int size;

	/** фактор агруженности таблицы */
	private volatile float loadFactor;

	@SuppressWarnings("unchecked")
	public ConcurrentTable()
	{
		// определяем фактор загруженности
		loadFactor = DEFAULT_LOAD_FACTOR;

		// определяем размер таблицы, при котором произойдет ресайз
		threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);

		ReadWriteLock readWriteLock = Locks.newRWLock();

		readLock = readWriteLock.readLock();
		writeLock = readWriteLock.writeLock();

		// создаем массив ячеяк
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
		writeLock.lock();
		try
		{
			// получаем текущую нужную ячейку
			Entry<V> entry = table[index];

			// вносим новую
			table[index] = new Entry<V>(key, value, entry, hash);

			// если размер превысил допустимый размер
			if(size++ >= threshold)
				// обновляем размер табилцы
				resize(2 * table.length);
		}
		finally
		{
			writeLock.unlock();
		}
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
		writeLock.lock();
		try
		{
			// очищаем таблицу
			Arrays.clear(table);

			// зануляем счетчик
			size = 0;
		}
		finally
		{
			writeLock.unlock();
		}
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
		readLock.lock();
		try
		{
			// перебираем ячейки
			for(int i = 0, length = table.length; i < length; i++)
			{
				// получаем ячейку в таблице
				Entry<V> element = table[i];

				// пробегаемся по цепочке ячеяк
				for(Entry<V> entry = element; entry != null; entry = entry.next)
					// если находим ячейку с нужным значением
					if(value.equals(entry.value))
						return true;
			}

			return false;
		}
		finally
		{
			readLock.unlock();
		}
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
		// расчитываем новый хэш
		int hash = Tables.hash(key);

		readLock.lock();
		try
		{
			// перебираем цепочку ячеяк в поисках нужной
			for(Entry<V> entry = table[Tables.indexFor(hash, table.length)]; entry != null; entry = entry.next)
				if(entry.hash == hash && entry.key == key)
					return entry.value;

			return null;
		}
		finally
		{
			readLock.unlock();
		}
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

	private Entry<V> getEntry(int key)
	{
		int hash = Tables.hash(key);

		readLock.lock();
		try
		{
			for(Entry<V> entry = table[Tables.indexFor(hash, table.length)]; entry != null; entry = entry.next)
				if(entry.key == key)
					return entry;

			return null;
		}
		finally
		{
			readLock.unlock();
		}
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
	public Iterator<V> iterator()
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
		int hash = Tables.hash(key);

		writeLock.lock();
		try
		{
			int i = Tables.indexFor(hash, table.length);

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
		finally
		{
			writeLock.unlock();
		}
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
	public void readLock()
	{
		readLock.lock();
	}

	@Override
	public void readUnlock()
	{
		readLock.unlock();
	}

	@Override
	public void reinit(){}

	@Override
	public V remove(int key)
	{
		writeLock.lock();
		try
		{
			Entry<V> e = removeEntryForKey(key);

			return e == null ? null : e.value;
		}
		finally
		{
			writeLock.unlock();
		}
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
		int hash = Tables.hash(key);

		int i = Tables.indexFor(hash, table.length);

		writeLock.lock();
		try
		{
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
		finally
		{
			writeLock.unlock();
		}
	}

	/**
	 * Изминение размера таблицы.
	 *
	 * @param newLength новый размер.
	 */
	@SuppressWarnings("unchecked")
	private void resize(int newLength)
	{
		writeLock.lock();
		try
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
		finally
		{
			writeLock.unlock();
		}
	}

	@Override
	public int size()
	{
		return size;
	}

	/**
	 * Перенос всез записей из старой таблице в новую
	 *
	 * @param newTable
	 */
	private void transfer(Entry<V>[] newTable)
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

					int i = Tables.indexFor(entry.hash, newCapacity);

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
	public void writeLock()
	{
		writeLock.lock();
	}

	@Override
	public void writeUnlock()
	{
		writeLock.unlock();
	}
}