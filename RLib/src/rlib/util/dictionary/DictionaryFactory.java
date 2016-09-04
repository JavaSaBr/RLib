package rlib.util.dictionary;

/**
 * The factory for creating new {@link Dictionary}.
 *
 * @author JavaSaBr
 */
public final class DictionaryFactory {

    /**
     * @return the new {@link FinalConcurrentAtomicARSWLockIntegerDictionary}.
     */
    public static <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary() {
        return new FinalConcurrentAtomicARSWLockIntegerDictionary<>();
    }

    /**
     * @return the new {@link FinalConcurrentAtomicARSWLockLongDictionary}.
     */
    public static <V> ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary() {
        return new FinalConcurrentAtomicARSWLockLongDictionary<>();
    }

    /**
     * @return the new {@link FinalConcurrentAtomicARSWLockObjectDictionary}.
     */
    public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary() {
        return new FinalConcurrentAtomicARSWLockObjectDictionary<>();
    }

    /**
     * @return the new {@link FinalFastIntegerDictionary}.
     */
    public static <V> IntegerDictionary<V> newIntegerDictionary() {
        return new FinalFastIntegerDictionary<>();
    }

    /**
     * @return the new {@link FinalFastLongDictionary}.
     */
    public static <V> LongDictionary<V> newLongDictionary() {
        return new FinalFastLongDictionary<>();
    }

    /**
     * @return the new {@link FinalFastObjectDictionary}.
     */
    public static <K, V> ObjectDictionary<K, V> newObjectDictionary() {
        return new FinalFastObjectDictionary<>();
    }

    private DictionaryFactory() {
        throw new IllegalArgumentException();
    }
}
