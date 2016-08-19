package rlib.util.dictionary;

/**
 * Базовая реализация слваря.
 *
 * @author JavaSaBr
 */
public abstract class AbstractDictionary<K, V> implements Dictionary<K, V> {

    /**
     * Размер таблицы по умолчанию.
     */
    protected static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * Максимальный размер таблицы.
     */
    protected static final int DEFAULT_MAXIMUM_CAPACITY = 1 << 30;

    /**
     * Фактор загружености таблицы, для определения момента ее расширения.
     */
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * Детализированный рассчет хэша.
     *
     * @param hash начальный хэш.
     * @return новый хэш.
     */
    protected static int hash(int hash) {
        hash ^= hash >>> 20 ^ hash >>> 12;
        return hash ^ hash >>> 7 ^ hash >>> 4;
    }

    /**
     * Детализированный рассчет хэша.
     *
     * @param key лонг ключ.
     * @return новый хэш.
     */
    protected static int hash(final long key) {
        int hash = (int) (key ^ key >>> 32);
        hash ^= hash >>> 20 ^ hash >>> 12;
        return hash ^ hash >>> 7 ^ hash >>> 4;
    }

    /**
     * Определние индекса ячейки по хэш коду.
     *
     * @param hash   хеш ключа.
     * @param length длина массива.
     * @return индекс ячейки.
     */
    protected static int indexFor(final int hash, final int length) {
        return hash & length - 1;
    }

    protected abstract int decrementSizeAndGet();

    protected abstract int incrementSizeAndGet();
}
