package rlib.util.dictionary;

/**
 * Фабрика по созаднию словарей.
 *
 * @author Ronn
 */
public final class DictionaryFactory {

    /**
     * @return новый {@link ConcurrentAtomicIntegerDictionary}.
     */
    public static <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary() {
        return new ConcurrentAtomicIntegerDictionary<>();
    }

    /**
     * @return новый {@link ConcurrentLockLongDictionary}.
     */
    public static <V> ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary() {
        return new ConcurrentAtomicLongDictionary<>();
    }

    /**
     * @return новый {@link ConcurrentAtomicObjectDictionary}.
     */
    public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary() {
        return new ConcurrentAtomicObjectDictionary<>();
    }

    /**
     * @return новый {@link ConcurrentLockIntegerDictionary}.
     */
    public static <V> ConcurrentIntegerDictionary<V> newConcurrentIntegerDictionary() {
        return new ConcurrentLockIntegerDictionary<>();
    }

    /**
     * @return новый {@link ConcurrentLockLongDictionary}.
     */
    public static <V> ConcurrentLongDictionary<V> newConcurrentLongDictionary() {
        return new ConcurrentLockLongDictionary<>();
    }

    /**
     * @return новый {@link ConcurrentLockObjectDictionary}.
     */
    public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentObjectDictionary() {
        return new ConcurrentLockObjectDictionary<>();
    }

    /**
     * @return новый {@link FastIntegerDictionary}.
     */
    public static <V> IntegerDictionary<V> newIntegerDictionary() {
        return new FastIntegerDictionary<>();
    }

    /**
     * @return новый {@link FastLongDictionary}.
     */
    public static <V> LongDictionary<V> newLongDictionary() {
        return new FastLongDictionary<>();
    }

    /**
     * @return новый {@link FastObjectDictionary}.
     */
    public static <K, V> ObjectDictionary<K, V> newObjectDictionary() {
        return new FastObjectDictionary<>();
    }

    private DictionaryFactory() {
        throw new IllegalArgumentException();
    }
}
