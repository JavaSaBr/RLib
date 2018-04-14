package com.ss.rlib.common.util.dictionary;

/**
 * The final implementation of {@link ConcurrentAtomicARSWLockIntegerDictionary}.
 *
 * @param <V> the type parameter
 * @author Ronn
 */
public final class FinalConcurrentAtomicARSWLockIntegerDictionary<V> extends ConcurrentAtomicARSWLockIntegerDictionary<V> {

    /**
     * Instantiates a new Final concurrent atomic arsw lock integer dictionary.
     */
    public FinalConcurrentAtomicARSWLockIntegerDictionary() {
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock integer dictionary.
     *
     * @param loadFactor the load factor
     */
    public FinalConcurrentAtomicARSWLockIntegerDictionary(float loadFactor) {
        super(loadFactor);
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock integer dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    public FinalConcurrentAtomicARSWLockIntegerDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock integer dictionary.
     *
     * @param initCapacity the init capacity
     */
    public FinalConcurrentAtomicARSWLockIntegerDictionary(int initCapacity) {
        super(initCapacity);
    }
}
