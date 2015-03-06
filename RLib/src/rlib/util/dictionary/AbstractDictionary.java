package rlib.util.dictionary;

/**
 * Базовая реализация слваря.
 * 
 * @author Ronn
 */
public abstract class AbstractDictionary<K, V> implements Dictionary<K, V> {

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
	protected final static int hash(int hash) {
		hash ^= hash >>> 20 ^ hash >>> 12;
		return hash ^ hash >>> 7 ^ hash >>> 4;
	}

	/**
	 * Детализированный рассчет хэша.
	 * 
	 * @param key лонг ключ.
	 * @return новый хэш.
	 */
	protected final static int hash(final long key) {
		int hash = (int) (key ^ key >>> 32);
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
	protected final static int indexFor(final int hash, final int length) {
		return hash & length - 1;
	}

	protected abstract int incrementSizeAndGet();

	protected abstract int decrementSizeAndGet();
}
