package rlib.util.dictionary;

/**
 * Фабрика по созаднию словарей.
 *
 * @author JavaSaBr
 */
public final class DictionaryFactory {

    /**
     * @return новый {@link ConcurrentAtomicIntegerDictionary}.
     */
    public static <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary() {
        return new ConcurrentAtomicIntegerDictionary<>();
    }

    /**
     * @return новый {@link ConcurrentReentrantReadWriteLockLongDictionary}.
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
     * @return новый {@link ConcurrentReentrantReadWriteLockIntegerDictionary}.
     */
    public static <V> ConcurrentIntegerDictionary<V> newConcurrentIntegerDictionary() {
        return new ConcurrentReentrantReadWriteLockIntegerDictionary<>();
    }

    /**
     * @return новый {@link ConcurrentReentrantReadWriteLockLongDictionary}.
     */
    public static <V> ConcurrentLongDictionary<V> newConcurrentLongDictionary() {
        return new ConcurrentReentrantReadWriteLockLongDictionary<>();
    }

    /**
     * @return новый {@link ConcurrentReentrantReadWriteLockObjectDictionary}.
     */
    public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentObjectDictionary() {
        return new ConcurrentReentrantReadWriteLockObjectDictionary<>();
    }

    /**
     * @return новый {@link FastIntegerDictionary}.
     */
    public static <V> IntegerDictionary<V> newIntegerDictionary() {
        return new FinalFastIntegerDictionary<>();
    }

    /**
     * @return новый {@link FastLongDictionary}.
     */
    public static <V> LongDictionary<V> newLongDictionary() {
        return new FinalFastLongDictionary<>();
    }

    /**
     * @return новый {@link FastObjectDictionary}.
     */
    public static <K, V> ObjectDictionary<K, V> newObjectDictionary() {
        return new FinalFastObjectDictionary<>();
    }

    private DictionaryFactory() {
        throw new IllegalArgumentException();
    }
}
