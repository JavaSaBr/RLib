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
     * @return the new {@link ConcurrentAtomicARSWLockIntegerDictionary}.
     */
    public static <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary() {
        return new ConcurrentAtomicARSWLockIntegerDictionary<>();
    }

    /**
     * New concurrent atomic integer dictionary concurrent integer dictionary.
     *
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link ConcurrentAtomicARSWLockIntegerDictionary}.
     */
    public static <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary(
            float loadFactor,
            int initCapacity
    ) {
        return new ConcurrentAtomicARSWLockIntegerDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New concurrent atomic long dictionary concurrent long dictionary.
     *
     * @param <V> the type parameter
     * @return the new {@link ConcurrentAtomicARSWLockLongDictionary}.
     */
    public static <V> ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary() {
        return new ConcurrentAtomicARSWLockLongDictionary<>();
    }

    /**
     * New concurrent atomic long dictionary concurrent long dictionary.
     *
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link ConcurrentAtomicARSWLockLongDictionary}.
     */
    public static <V> ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary(
            float loadFactor,
            int initCapacity
    ) {
        return new ConcurrentAtomicARSWLockLongDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New concurrent atomic object dictionary concurrent object dictionary.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @return the new {@link ConcurrentAtomicARSWLockObjectDictionary}.
     */
    public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary() {
        return new ConcurrentAtomicARSWLockObjectDictionary<>();
    }

    /**
     * New concurrent atomic object dictionary concurrent object dictionary.
     *
     * @param <K>          the type parameter
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link ConcurrentAtomicARSWLockLongDictionary}.
     */
    public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary(
            float loadFactor,
            int initCapacity
    ) {
        return new ConcurrentAtomicARSWLockObjectDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New integer dictionary integer dictionary.
     *
     * @param <V> the type parameter
     * @return the new {@link FastIntegerDictionary}.
     */
    public static <V> IntegerDictionary<V> newIntegerDictionary() {
        return new FastIntegerDictionary<>();
    }

    /**
     * New integer dictionary integer dictionary.
     *
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link FastIntegerDictionary}.
     */
    public static <V> IntegerDictionary<V> newIntegerDictionary(float loadFactor, int initCapacity) {
        return new FastIntegerDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New long dictionary long dictionary.
     *
     * @param <V> the type parameter
     * @return the new {@link FastLongDictionary}.
     */
    public static <V> LongDictionary<V> newLongDictionary() {
        return new FastLongDictionary<>();
    }

    /**
     * New long dictionary long dictionary.
     *
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link FastLongDictionary}.
     */
    public static <V> LongDictionary<V> newLongDictionary(float loadFactor, int initCapacity) {
        return new FastLongDictionary<>(loadFactor, initCapacity);
    }

    /**
     * New object dictionary object dictionary.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @return the new {@link FastObjectDictionary}.
     */
    public static <K, V> ObjectDictionary<K, V> newObjectDictionary() {
        return new FastObjectDictionary<>();
    }

    /**
     * New object dictionary object dictionary.
     *
     * @param <K>          the type parameter
     * @param <V>          the type parameter
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     * @return the new {@link FastObjectDictionary}.
     */
    public static <K, V> ObjectDictionary<K, V> newObjectDictionary(float loadFactor, int initCapacity) {
        return new FastObjectDictionary<>(loadFactor, initCapacity);
    }

    private DictionaryFactory() {
        throw new IllegalArgumentException();
    }
}
