package rlib.util.table;

/**
 * Класс для работы с коллекциями Table.
 *
 * @author Ronn
 */
public final class Tables
{
	/**
	 * Дополнительный рассчет хэша для таблицы.
	 *
	 * @param hash начальный хэш.
	 * @return новый хэш.
	 */
	public final static int hash(int hash)
	{
		hash ^= hash >>> 20 ^ hash >>> 12;

		return hash ^ hash >>> 7 ^ hash >>> 4;
	}

	/**
	 * @return индекс ячейки в таблице.
	 */
	public final static int indexFor(int hash, int length)
	{
		return hash & length - 1;
	}

	/**
	 * Создание таблицы с int ключем.
	 *
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newConcurrentIntegerTable()
	{
		return new ConcurrentIntegerTable<V>();
	}

	/**
	 * Создание таблицы с int ключем.
	 *
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newConcurrentIntegerTable(float loadFactor)
	{
		return new ConcurrentIntegerTable<V>(loadFactor);
	}

	/**
	 * Создание таблицы с int ключем.
	 *
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newConcurrentIntegerTable(int initCapacity)
	{
		return new ConcurrentIntegerTable<V>(initCapacity);
	}

	/**
	 * Создание таблицы с long ключем.
	 *
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newConcurrentLongTable()
	{
		return new ConcurrentLongTable<V>();
	}

	/**
	 * Создание таблицы с long ключем.
	 *
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newConcurrentLongTable(float loadFactor)
	{
		return new ConcurrentLongTable<V>(loadFactor);
	}

	/**
	 * Создание таблицы с long ключем.
	 *
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newConcurrentLongTable(int initCapacity)
	{
		return new ConcurrentLongTable<V>(initCapacity);
	}

	/**
	 * Создание таблицы с объектным ключем.
	 *
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newConcurrentObjectTable()
	{
		return new ConcurrentObjectTable<K, V>();
	}

	/**
	 * Создание таблицы с объектным ключем.
	 *
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newConcurrentObjectTable(float loadFactor)
	{
		return new ConcurrentObjectTable<K, V>(loadFactor);
	}

	/**
	 * Создание таблицы с объектным ключем.
	 *
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newConcurrentObjectTable(int initCapacity)
	{
		return new ConcurrentObjectTable<K, V>(initCapacity);
	}

	/**
	 * Создание новой таблицы с указанным типом ключа и значения.
	 *
	 * @param key тип ключа.
	 * @param value тип значения.
	 * @return новая таблица.
	 * @deprecated
	 */
	public static final <K, V> Table<K, V> newConcurrentTable()
	{
		return new ConcurrentTable<K, V>();
	}

	/**
	 * Создание таблицы с int ключем.
	 *
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newIntegerTable()
	{
		return new FastIntegerTable<V>();
	}

	/**
	 * Создание таблицы с int ключем.
	 *
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newIntegerTable(float loadFactor)
	{
		return new FastIntegerTable<V>(loadFactor);
	}

	/**
	 * Создание таблицы с int ключем.
	 *
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newIntegerTable(int initCapacity)
	{
		return new FastIntegerTable<V>(initCapacity);
	}

	/**
	 * Создание таблицы с long ключем.
	 *
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newLongTable()
	{
		return new FastLongTable<V>();
	}

	/**
	 * Создание таблицы с int ключем.
	 *
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newLongTable(float loadFactor)
	{
		return new FastLongTable<V>(loadFactor);
	}

	/**
	 * Создание таблицы с long ключем.
	 *
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newLongTable(int initCapacity)
	{
		return new FastLongTable<V>(initCapacity);
	}

	/**
	 * Создание таблицы с объектным ключем.
	 *
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newObjectTable()
	{
		return new FastObjectTable<K, V>();
	}

	/**
	 * Создание таблицы с объектным ключем.
	 *
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newObjectTable(float loadFactor)
	{
		return new FastObjectTable<K, V>(loadFactor);
	}

	/**
	 * Создание таблицы с объектным ключем.
	 *
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newObjectTable(int initCapacity)
	{
		return new FastObjectTable<K, V>(initCapacity);
	}

	/**
	 * Создание новой таблицы с указанным типом ключа и значения.
	 *
	 * @param key тип ключа.
	 * @param value тип значения.
	 * @return новая таблица.
	 * @deprecated
	 */
	public static final <K, V> Table<K, V> newTable()
	{
		return new FastTable<K, V>();
	}

	private Tables()
	{
		throw new IllegalArgumentException();
	}
}
