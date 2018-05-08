package com.ss.rlib.common.util.dictionary;

/**
 * The final implementation of {@link FastIntegerDictionary}.
 *
 * @param <V> the type parameter
 * @author Ronn
 */
public final class FinalFastIntegerDictionary<V> extends FastIntegerDictionary<V> {

    /**
     * Instantiates a new Final fast integer dictionary.
     */
    public FinalFastIntegerDictionary() {
    }

    /**
     * Instantiates a new Final fast integer dictionary.
     *
     * @param loadFactor the load factor
     */
    public FinalFastIntegerDictionary(float loadFactor) {
        super(loadFactor);
    }

    /**
     * Instantiates a new Final fast integer dictionary.
     *
     * @param initCapacity the init capacity
     */
    public FinalFastIntegerDictionary(int initCapacity) {
        super(initCapacity);
    }

    /**
     * Instantiates a new Final fast integer dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    public FinalFastIntegerDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }
}
