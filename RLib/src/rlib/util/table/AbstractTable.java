package rlib.util.table;

import rlib.util.array.Array;
import rlib.util.array.IntegerArray;
import rlib.util.array.LongArray;

/**
 * Базовая модель таблицы.
 * 
 * @author Ronn
 */
public abstract class AbstractTable<K, V> implements Table<K, V>
{
	/** размер таблицы по умолчанию */
	protected static final int DEFAULT_INITIAL_CAPACITY = 16;
	
	/** максимальный размер таблицы */
	protected static final int DEFAULT_MAXIMUM_CAPACITY = 1 << 30;
	
	/** фактор загружености таблицы, для определения момента ее расширения */
	protected static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
	/**
	 * Детализированный рассчет хэша.
	 * 
	 * @param hash начальный хэш.
	 * @return новый хэш.
	 */
	protected final static int hash(int hash)
	{
		hash ^= hash >>> 20 ^ hash >>> 12;
		return hash ^ hash >>> 7 ^ hash >>> 4;
	}
	
	/**
	 * Детализированный рассчет хэша.
	 * 
	 * @param key лонг ключ.
	 * @return новый хэш.
	 */
	protected final static int hash(long key)
	{
		int hash = (int)(key ^ (key >>> 32));
		
		hash ^= hash >>> 20 ^ hash >>> 12;
		
		return hash ^ hash >>> 7 ^ hash >>> 4;
	}
	
	/**
	 * Определние индекса ячейки по хэш коду.
	 * 
	 * @param хеш ключа.
	 * @param длинна массива.
	 * @return индекс ячейки.
	 */
	protected final static int indexFor(int hash, int length)
	{
		return hash & length - 1;
	}
	
	@Override
	public boolean containsKey(int key)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public boolean containsKey(K key)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public boolean containsKey(long key)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public boolean containsValue(V value)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public void finalyze()
	{
		clear();
	}

	@Override
	public V get(int key)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public V get(K key)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public V get(long key)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public boolean isEmpty()
	{
		return size() < 1;
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
	public V put(int key, V value)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public V put(K key, V value)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public V put(long key, V value)
	{
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public void put(Table<K, V> table)
	{
		table.moveTo(this);
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
		throw new IllegalArgumentException("not supported.");
	}

	@Override
	public V remove(K key)
	{
		throw new IllegalArgumentException("not supported.");
	}
	
	@Override
	public V remove(long key)
	{
		throw new IllegalArgumentException("not supported.");
	}
	
	@Override
	public void writeLock(){}
	
	@Override
	public void writeUnlock(){}
}
