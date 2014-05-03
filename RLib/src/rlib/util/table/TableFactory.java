package rlib.util.table;

/**
 * Класс для работы с коллекциями Table.
 * 
 * @author Ronn
 */
public final class TableFactory {

	/**
	 * Создание таблицы с int ключем.
	 * 
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newConcurrentIntegerTable() {
		return new ConcurrentIntegerTable<V>();
	}

	/**
	 * Создание таблицы с int ключем.
	 * 
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newConcurrentIntegerTable(final float loadFactor) {
		return new ConcurrentIntegerTable<V>(loadFactor);
	}

	/**
	 * Создание таблицы с int ключем.
	 * 
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newConcurrentIntegerTable(final int initCapacity) {
		return new ConcurrentIntegerTable<V>(initCapacity);
	}

	/**
	 * Создание таблицы с long ключем.
	 * 
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newConcurrentLongTable() {
		return new ConcurrentLongTable<V>();
	}

	/**
	 * Создание таблицы с long ключем.
	 * 
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newConcurrentLongTable(final float loadFactor) {
		return new ConcurrentLongTable<V>(loadFactor);
	}

	/**
	 * Создание таблицы с long ключем.
	 * 
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newConcurrentLongTable(final int initCapacity) {
		return new ConcurrentLongTable<V>(initCapacity);
	}

	/**
	 * Создание таблицы с объектным ключем.
	 * 
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newConcurrentObjectTable() {
		return new ConcurrentObjectTable<K, V>();
	}

	/**
	 * Создание таблицы с объектным ключем.
	 * 
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newConcurrentObjectTable(final float loadFactor) {
		return new ConcurrentObjectTable<K, V>(loadFactor);
	}

	/**
	 * Создание таблицы с объектным ключем.
	 * 
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newConcurrentObjectTable(final int initCapacity) {
		return new ConcurrentObjectTable<K, V>(initCapacity);
	}

	/**
	 * Создание таблицы с int ключем.
	 * 
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newIntegerTable() {
		return new FastIntegerTable<V>();
	}

	/**
	 * Создание таблицы с int ключем.
	 * 
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newIntegerTable(final float loadFactor) {
		return new FastIntegerTable<V>(loadFactor);
	}

	/**
	 * Создание таблицы с int ключем.
	 * 
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<IntKey, V> newIntegerTable(final int initCapacity) {
		return new FastIntegerTable<V>(initCapacity);
	}

	/**
	 * Создание таблицы с long ключем.
	 * 
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newLongTable() {
		return new FastLongTable<V>();
	}

	/**
	 * Создание таблицы с int ключем.
	 * 
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newLongTable(final float loadFactor) {
		return new FastLongTable<V>(loadFactor);
	}

	/**
	 * Создание таблицы с long ключем.
	 * 
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <V> Table<LongKey, V> newLongTable(final int initCapacity) {
		return new FastLongTable<V>(initCapacity);
	}

	/**
	 * Создание таблицы с объектным ключем.
	 * 
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newObjectTable() {
		return new FastObjectTable<K, V>();
	}

	/**
	 * Создание таблицы с объектным ключем.
	 * 
	 * @param loadFactor фактор загруженности таблицы.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newObjectTable(final float loadFactor) {
		return new FastObjectTable<K, V>(loadFactor);
	}

	/**
	 * Создание таблицы с объектным ключем.
	 * 
	 * @param initCapacity начальный размер таблицы ячеяк.
	 * @return новая таблица с объектным ключем.
	 */
	public static final <K, V> Table<K, V> newObjectTable(final int initCapacity) {
		return new FastObjectTable<K, V>(initCapacity);
	}

	private TableFactory() {
		throw new IllegalArgumentException();
	}
}
