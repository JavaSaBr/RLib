package com.ss.rlib.common.util.dictionary;

/**
 * The final implementation of {@link FinalFastLongDictionary}.
 *
 * @param <V> the type parameter
 * @author Ronn
 */
public final class FinalFastLongDictionary<V> extends FastLongDictionary<V> {

    /**
     * Instantiates a new Final fast long dictionary.
     */
    public FinalFastLongDictionary() {
    }

    /**
     * Instantiates a new Final fast long dictionary.
     *
     * @param loadFactor the load factor
     */
    public FinalFastLongDictionary(final float loadFactor) {
        super(loadFactor);
    }

    /**
     * Instantiates a new Final fast long dictionary.
     *
     * @param initCapacity the init capacity
     */
    public FinalFastLongDictionary(final int initCapacity) {
        super(initCapacity);
    }

    /**
     * Instantiates a new Final fast long dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    public FinalFastLongDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }
}
