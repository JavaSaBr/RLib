package com.ss.rlib.util.dictionary;

/**
 * The final implementation of {@link ConcurrentAtomicARSWLockLongDictionary}.
 *
 * @param <V> the type parameter
 * @author Ronn
 */
public final class FinalConcurrentAtomicARSWLockLongDictionary<V> extends ConcurrentAtomicARSWLockLongDictionary<V> {

    /**
     * Instantiates a new Final concurrent atomic arsw lock long dictionary.
     */
    public FinalConcurrentAtomicARSWLockLongDictionary() {
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock long dictionary.
     *
     * @param loadFactor the load factor
     */
    public FinalConcurrentAtomicARSWLockLongDictionary(float loadFactor) {
        super(loadFactor);
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock long dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    public FinalConcurrentAtomicARSWLockLongDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock long dictionary.
     *
     * @param initCapacity the init capacity
     */
    public FinalConcurrentAtomicARSWLockLongDictionary(int initCapacity) {
        super(initCapacity);
    }
}
