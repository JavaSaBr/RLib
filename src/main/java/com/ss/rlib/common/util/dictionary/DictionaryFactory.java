package com.ss.rlib.common.util.dictionary;

/**
 * The factory for creating new {@link Dictionary}.
 *
 * @author JavaSaBr
 */
public final class DictionaryFactory {

    /**
     * New concurrent atomic integer dictionary concurrent integer dictionary.
     *
     * @param <V> the type parameter
     * @return the new {@link FinalConcurrentAtomicARSWLockIntegerDictionary}.
     */
    public static <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary() {
        return new FinalConcurrentAtomicARSWLockIntegerDictionary<>();
    }

    /**
     * New concurrent atomic integer dictionary concurrent integer dictionary.
     *
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link FinalConcurrentAtomicARSWLockIntegerDictionary}.
     */
    public static <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary(final float loadFactor, final int initCapacity) {
        return new FinalConcurrentAtomicARSWLockIntegerDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New concurrent atomic long dictionary concurrent long dictionary.
     *
     * @param <V> the type parameter
     * @return the new {@link FinalConcurrentAtomicARSWLockLongDictionary}.
     */
    public static <V> ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary() {
        return new FinalConcurrentAtomicARSWLockLongDictionary<>();
    }

    /**
     * New concurrent atomic long dictionary concurrent long dictionary.
     *
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link FinalConcurrentAtomicARSWLockLongDictionary}.
     */
    public static <V> ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary(final float loadFactor, final int initCapacity) {
        return new FinalConcurrentAtomicARSWLockLongDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New concurrent atomic object dictionary concurrent object dictionary.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @return the new {@link FinalConcurrentAtomicARSWLockObjectDictionary}.
     */
    public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary() {
        return new FinalConcurrentAtomicARSWLockObjectDictionary<>();
    }

    /**
     * New concurrent atomic object dictionary concurrent object dictionary.
     *
     * @param <K>          the type parameter
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link FinalConcurrentAtomicARSWLockLongDictionary}.
     */
    public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary(final float loadFactor, final int initCapacity) {
        return new FinalConcurrentAtomicARSWLockObjectDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New integer dictionary integer dictionary.
     *
     * @param <V> the type parameter
     * @return the new {@link FinalFastIntegerDictionary}.
     */
    public static <V> IntegerDictionary<V> newIntegerDictionary() {
        return new FinalFastIntegerDictionary<>();
    }

    /**
     * New integer dictionary integer dictionary.
     *
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link FinalFastIntegerDictionary}.
     */
    public static <V> IntegerDictionary<V> newIntegerDictionary(final float loadFactor, final int initCapacity) {
        return new FinalFastIntegerDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New long dictionary long dictionary.
     *
     * @param <V> the type parameter
     * @return the new {@link FinalFastLongDictionary}.
     */
    public static <V> LongDictionary<V> newLongDictionary() {
        return new FinalFastLongDictionary<>();
    }

    /**
     * New long dictionary long dictionary.
     *
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link FinalFastLongDictionary}.
     */
    public static <V> LongDictionary<V> newLongDictionary(final float loadFactor, final int initCapacity) {
        return new FinalFastLongDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New object dictionary object dictionary.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @return the new {@link FinalFastObjectDictionary}.
     */
    public static <K, V> ObjectDictionary<K, V> newObjectDictionary() {
        return new FinalFastObjectDictionary<>();
    }

    /**
     * New object dictionary object dictionary.
     *
     * @param <K>          the type parameter
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link FinalFastObjectDictionary}.
     */
    public static <K, V> ObjectDictionary<K, V> newObjectDictionary(final float loadFactor, final int initCapacity) {
        return new FinalFastObjectDictionary<>(loadFactor, initCapacity);
    }

    private DictionaryFactory() {
        throw new IllegalArgumentException();
    }
}
