package rlib.util.table;

import java.util.Iterator;
import java.util.NoSuchElementException;

import rlib.concurrent.AsynReadSynWriteLock;
import rlib.concurrent.Locks;
import rlib.util.array.Array;
import rlib.util.array.Arrays;
import rlib.util.array.LongArray;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;
import rlib.util.pools.Pools;

/**
 * Модель конкурентной таблицы с примитивным long числом ключем.
 *
 * @author Ronn
 */
public class ConcurrentLongTable<V> extends AbstractTable<LongKey, V>
{
	/**
	 * Модель ячейки в таблице.
	 *
	 * @author Ronn
	 */
	private final static class Entry<V> implements Foldable
	{
		/** следующая ячейка */
		private Entry<V> next;

		/** значение */
		private V value;

		/** ключ */
		private long key;

		/** хэш ключа */
		private int hash;

        @Override
		public boolean equals(Object object)
		{
			if(object == null || object.getClass() != Entry.class)
				return false;

			Entry<?> entry = (Entry<?>) object;

			// получаем первый ключ
			long firstKey = getKey();
			// получаем второй ключ
			long secondKey = entry.getKey();

			// если ключи совпадают
			if(firstKey == secondKey)
			{
				// получаем первое значение
				Object firstValue = getValue();
				// получаем второе значение
				Object secondValue = entry.getValue();

				// сли значения совпадают
				if(firstValue == secondValue || (firstValue != null && firstValue.equals(secondValue)))
					return true;
			}

			return false;
		}

		@Override
		public void finalyze()
		{
			value = null;
			next = null;
			key = 0;
			hash = 0;
		}

		/**
		 * @return хэш ячейки.
		 */
		public int getHash()
		{
			return hash;
		}

		/**
		 * @return ключ ячейки.
		 */
		public long getKey()
		{
			return key;
		}

		/**
		 * @return следующая ячейка.
		 */
		public Entry<V> getNext()
		{
			return next;
		}

		/**
		 * @return значение ячейки.
		 */
		public V getValue()
		{
			return value;
		}

		@Override
		public final int hashCode()
		{
            return (int) (key ^ (value == null ? 0 : value.hashCode()));
        }

		@Override
		public void reinit()
		{
			hash = 0;
		}

		public void set(int hash, long key, V value, Entry<V> next)
        {
            this.value = value;
            this.next = next;
            this.key = key;
            this.hash = hash;
        }

		/**
		 * @param next следующая цепочка.
		 */
		public void setNext(Entry<V> next)
		{
			this.next = next;
		}

		/**
		 * Установка нового значения.
		 *
		 * @param value новое значение.
		 * @return старое значение.
		 */
		public V setValue(V value)
		{
			V old = getValue();

			this.value = value;

			return old;
		}

		@Override
		public final String toString()
		{
			return "Entry : " + key + " = " + value;
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

		/** текущий entry */
		private Entry<V> current;

		/** текущий индекс в таблице */
		private int index;

		private TableIterator()
		{
			// получаем таблицу ячеяк
			Entry<V>[] table = table();

			// если есть элементы
			if(size > 0)
				// исчем первую занятую ячейку
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
			return nextEntry().getValue();
		}

		/**
		 * @return следующая занятая ячейка.
		 */
		private Entry<V> nextEntry()
		{
			// получаем таблицу
			Entry<V>[] table = table();
			// получаем ячейку
			Entry<V> entry = next;

			// если ячейки нет, выходим
			if(entry == null)
				throw new NoSuchElementException();

			// перебираем цепочку
			if((next = entry.getNext()) == null)
				while(index < table.length && (next = table[index++]) == null);

			// запоминаем текущую
			current = entry;

			return entry;
		}

		@Override
		public void remove()
		{
			if(current == null)
				throw new IllegalStateException();

			long key = current.getKey();
			current = null;

			removeEntryForKey(key);
		}
	}

	/** пул ячеяк */
	private final FoldablePool<Entry<V>> entryPool;

	/** блокировщик */
	private final AsynReadSynWriteLock locker;

	/** таблица элементов */
	private volatile Entry<V>[] table;

	/** следующий размер для метода изминения размера (capacity * load factor) */
	private volatile int threshold;
	/** кол-во элементов в таблице */
	private volatile int size;

	/** фактор загружеености */
	private volatile float loadFactor;

	protected ConcurrentLongTable()
	{
		this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
	}

	protected ConcurrentLongTable(float loadFactor)
	{
		this(loadFactor, DEFAULT_INITIAL_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	protected ConcurrentLongTable(float loadFactor, int initCapacity)
	{
		this.loadFactor = loadFactor;
		this.threshold = (int) (initCapacity * loadFactor);

		size = 0;

		table = new Entry[DEFAULT_INITIAL_CAPACITY];
		entryPool = Pools.newFoldablePool(Entry.class);

		locker = Locks.newARSWLock();
	}

	protected ConcurrentLongTable(int initCapacity)
	{
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
	private final void addEntry(int hash, long key, V value, int index)
	{
		// получаем таблицу ячеяк
		Entry<V>[] table = table();

		// получаем текущую ячейку
		Entry<V> entry = table[index];

		// достаем из пула новую.
		Entry<V> newEntry = entryPool.take();

		// если в пуле небыло
		if(newEntry == null)
			// создаем новую
			newEntry = new Entry<V>();

		// вносим данные
		newEntry.set(hash, key, value, entry);

		// вносим в таблицу
		table[index] = newEntry;

		// если размер привысел загрузку
		if(size++ >= threshold)
			// переформировываем таблицу
			resize(2 * table.length);
	}

	@Override
	public void apply(FuncKeyValue<LongKey, V> func)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public void apply(FuncValue<V> func)
	{
		readLock();
		try
		{
			// получаем таблицу ячеяк
			Entry<V>[] table = table();

			// перебираем цепочки
			for(int i = 0, length = table.length; i < length; i++)
			{
				// получаем ячейку
				Entry<V> entry = table[i];

				// перебираем ее цепочку
				while(entry != null)
				{
					// применяем функцию
					func.apply(entry.getValue());

					// обновляем ячейку
					entry = entry.getNext();
				}
			}
		}
		finally
		{
			readUnlock();
		}
	}

	@Override
	public final void clear()
	{
		writeLock();
		try
		{
			// получаем таблицу ячеяк
			Entry<V>[] table = table();

			// следующая ячейка
			Entry<V> next = null;

			// перебираем ячейки
			for(int i = 0, length = table.length; i < length; i++)
			{
				// получаем ячейку
				Entry<V> entry = table[i];

				// если ячейка есть
				while(entry != null)
				{
					// получаем следующую ячейку от ее
					next = entry.getNext();

					// текущую ложим в пул
					entryPool.put(entry);

					// в текущую ложим следующую
					entry = next;
				}
			}

			// очищаем
			Arrays.clear(table);

			size = 0;
		}
		finally
		{
			writeUnlock();
		}
	}

	@Override
	public boolean containsKey(int key)
	{
		return containsKey((long) key);
	}

	@Override
	public final boolean containsKey(long key)
	{
		readLock();
		try
		{
			return getEntry(key) != null;
		}
		finally
		{
			readUnlock();
		}
	}

	@Override
	public final boolean containsValue(V value)
	{
		readLock();
		try
		{
			// если значение нул, выходим
			if(value == null)
				throw new NullPointerException("value is null.");

			// получаем таблицу ячеяк
			Entry<V>[] table = table();

			// перебираем ячейки
			for(int i = 0, length = table.length; i < length; i++)
			{
				// получаем ячейку
				Entry<V> element = table[i];

				// перебираем цепочку
				for(Entry<V> entry = element; entry != null; entry = entry.next)
					if(value.equals(entry.getValue()))
						return true;
			}

			return false;
		}
		finally
		{
			readUnlock();
		}
	}

	@Override
	public final void finalyze()
	{
		if(size() > 0)
			clear();
	}

	@Override
	public V get(int key)
	{
		return get((long) key);
	}

	@Override
	public final V get(long key)
	{
		readLock();
		try
		{
			// получаем ячейку
			Entry<V> entry = getEntry(key);

			// получаем значение из ячейки
			return entry == null? null : entry.getValue();
		}
		finally
		{
			readUnlock();
		}
	}

	/**
	 * Получение ячейки по ключу.
	 *
	 * @param key ключ ячейки.
	 * @return ячейка.
	 */
	private final Entry<V> getEntry(long key)
	{
		// рассчитываем хэш
		int hash = hash(key);

		// получаем таблицу ячеяк
		Entry<V>[] table = table();

		// перебираем цепочку ячеяк
		for(Entry<V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.getNext())
			if(entry.getHash() == hash && key == entry.getKey())
				return entry;

		return null;
	}

	@Override
	public TableType getType()
	{
		return TableType.LONG;
	}

	@Override
	public final Iterator<V> iterator()
	{
		return new TableIterator();
	}

	@Override
	public LongArray keyLongArray(LongArray container)
	{
		readLock();
		try
		{
			// получаем таблицу ячеяк
			Entry<V>[] table = table();

			// перебираем цепочки
			for(int i = 0, length = table.length; i < length; i++)
			{
				// получаем ячейку
				Entry<V> entry = table[i];

				// перебираем ее цепочку
				while(entry != null)
				{
					// добавлям ключ
					container.add(entry.getKey());

					// обновляем ячейку
					entry = entry.getNext();
				}
			}

			return container;
		}
		finally
		{
			readUnlock();
		}
	}

	@Override
	public void moveTo(Table<LongKey, V> table)
	{
		if(getType() != table.getType())
			throw new IllegalArgumentException("incorrect table type.");

		readLock();
		try
		{
			if(isEmpty())
				return;

			// получаем таблицу ячеяк
			Entry<V>[] entryes = table();

			// перебираем цепочки
			for(int i = 0, length = entryes.length; i < length; i++)
			{
				// получаем ячейку
				Entry<V> entry = entryes[i];

				// перебираем ее цепочку
				while(entry != null)
				{
					// вносим запись
					table.put(entry.getKey(), entry.getValue());

					// обновляем ячейку
					entry = entry.getNext();
				}
			}
		}
		finally
		{
			readUnlock();
		}
	}

	@Override
	public V put(int key, V value)
	{
		return put((long) key, value);
	}

	@Override
	public final V put(long key, V value)
	{
		writeLock();
		try
		{
			// рассчитываем хэш
			int hash = hash(key);

			// получаем таблицу ячеяк
			Entry<V>[] table = table();

			// рассчитываем позицию в таблице
			int i = indexFor(hash, table.length);

			// перебираем цепочку ячеяк
			for(Entry<V> entry = table[i]; entry != null; entry = entry.getNext())
				if(entry.getHash() == hash && key == entry.getKey())
					return entry.setValue(value);

			// добавляем новую ячейку
			addEntry(hash, key, value, i);

			return null;
		}
		finally
		{
			writeUnlock();
		}
	}

	@Override
	public void readLock()
	{
		locker.readLock();
	}

	@Override
	public void readUnlock()
	{
		locker.readUnlock();
	}

	@Override
	public V remove(int key)
	{
		return remove((long) key);
	}

	@Override
	public final V remove(long key)
	{
		writeLock();
		try
		{
			// удаляем ячейку по ключу
			Entry<V> old = removeEntryForKey(key);

			// если есть удаленная ячейка, запоминаем значение в ней
			V value = old == null? null : old.getValue();

			// ячейку ложим в пул
			entryPool.put(old);

			// возвращаем старое значение
			return value;
		}
		finally
		{
			writeUnlock();
		}
	}

	/**
	 * Удаление значения из ячейки по указанному ключу.
	 *
	 * @param key ключ ячейки.
	 * @return удаленная ячейка.
	 */
	private final Entry<V> removeEntryForKey(long key)
	{
		// определяем хеш ключа
		int hash = hash(key);

		// получаем таблицу ячеяк
		Entry<V>[] table = table();

		// определяем индекс цепочки
		int i = indexFor(hash, table.length);

		Entry<V> prev = table[i];
		Entry<V> entry = prev;

		// если ячейка есть
		while(entry != null)
		{
			// получаем следующую
			Entry<V> next = entry.getNext();

			// если это искомая ячейка
			if(entry.getHash() == hash && key == entry.getKey())
			{
				// уменьшаем размер таблицы
				size--;

				// обновляем цепочку
				if(prev == entry)
					table[i] = next;
				else
					prev.setNext(next);

				// возвращаем удаляемую ячейку
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
	private final void resize(int newLength)
	{
		// получаем старую таблицу
		Entry<V>[] oldTable = table();

		// получаем старый размер
		int oldLength = oldTable.length;

		// если размер таблицы достиг предела
		if(oldLength >= DEFAULT_MAXIMUM_CAPACITY)
		{
			// обновляем максимальную загруженность
			threshold = Integer.MAX_VALUE;
			return;
		}

		// создаем новую таблицу
		Entry<V>[] newTable = new Entry[newLength];

		// переносим данные в нее
		transfer(newTable);

		// сохраняем ее
		table = newTable;

		// обновляем максимальную загруженность
		threshold = (int) (newLength * loadFactor);
	}

	@Override
	public final int size()
	{
		return size;
	}

	/**
	 * @return массив ячеяк.
	 */
	private final Entry<V>[] table()
	{
		return table;
	}

	@Override
	public final String toString()
	{
		readLock();
		try
		{
			// создаем билдер строки
			StringBuilder builder = new StringBuilder(getClass().getSimpleName());

			// добавляем размер таблицы
			builder.append(" size = ").append(size).append(" : ");

			// получаем таблицу ячеяк
			Entry<V>[] table = table();

			// перебираем цепочки
			for(int i = 0, length = table.length; i < length; i++)
			{
				// получаем ячейку
				Entry<V> entry = table[i];

				// перебираем ее цепочку
				while(entry != null)
				{
					// вносим
					builder.append("[").append(entry.getKey()).append(" - ").append(entry.getValue()).append("]");
					// добавляес разделитель
					builder.append(", ");

					// обновляем ячейку
					entry = entry.getNext();
				}
			}

			if(size > 0)
				builder.replace(builder.length() - 2, builder.length(), ".");

			return builder.toString();
		}
		finally
		{
			readUnlock();
		}
	}

	/**
	 * Перенос всех записей из старой таблице в новую.
	 *
	 * @param newTable новая таблица.
	 */
	private final void transfer(Entry<V>[] newTable)
	{
		// получаем текущую таблицу
		Entry<V>[] original = table;

		// получаем размер новай таблицы
		int newCapacity = newTable.length;

		// перебираем старую таблицу
		for(int j = 0, length = original.length; j < length; j++)
		{
			// получаем ячейку
			Entry<V> entry = original[j];

			// если она есть
			if(entry != null)
			{
				do
				{
					// получаем след. ячейку
					Entry<V> next = entry.getNext();

					// получаем новый хэш ячейки
					int i = indexFor(entry.getHash(), newCapacity);

					// запоминаем след. ячейку
					entry.setNext(newTable[i]);

					// вносим в таблицу
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
		readLock();
		try
		{
			// получаем таблицу ячеяк
			Entry<V>[] table = table();

			// перебираем цепочки
			for(int i = 0, length = table.length; i < length; i++)
			{
				// получаем ячейку
				Entry<V> entry = table[i];

				// перебираем ее цепочку
				while(entry != null)
				{
					// добавлям ключ
					container.add(entry.getValue());

					// обновляем ячейку
					entry = entry.getNext();
				}
			}

			return container;
		}
		finally
		{
			readUnlock();
		}
	}

	@Override
	public void writeLock()
	{
		locker.writeLock();
	}

	@Override
	public void writeUnlock()
	{
		locker.writeUnlock();
	}
}
