package com.ss.rlib.util.dictionary;

/**
 * The final implementation of {@link ConcurrentAtomicARSWLockObjectDictionary}.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author Ronn
 */
public final class FinalConcurrentAtomicARSWLockObjectDictionary<K, V> extends ConcurrentAtomicARSWLockObjectDictionary<K, V> {

    /**
     * Instantiates a new Final concurrent atomic arsw lock object dictionary.
     */
    public FinalConcurrentAtomicARSWLockObjectDictionary() {
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock object dictionary.
     *
     * @param loadFactor the load factor
     */
    public FinalConcurrentAtomicARSWLockObjectDictionary(final float loadFactor) {
        super(loadFactor);
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock object dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    public FinalConcurrentAtomicARSWLockObjectDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock object dictionary.
     *
     * @param initCapacity the init capacity
     */
    public FinalConcurrentAtomicARSWLockObjectDictionary(final int initCapacity) {
        super(initCapacity);
    }
}
