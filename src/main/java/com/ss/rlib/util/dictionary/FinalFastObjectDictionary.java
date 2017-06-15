package com.ss.rlib.util.dictionary;

/**
 * The final implementation of {@link FastObjectDictionary}.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author Ronn
 */
public class FinalFastObjectDictionary<K, V> extends FastObjectDictionary<K, V> {

    /**
     * Instantiates a new Final fast object dictionary.
     */
    public FinalFastObjectDictionary() {
    }

    /**
     * Instantiates a new Final fast object dictionary.
     *
     * @param loadFactor the load factor
     */
    public FinalFastObjectDictionary(float loadFactor) {
        super(loadFactor);
    }

    /**
     * Instantiates a new Final fast object dictionary.
     *
     * @param initCapacity the init capacity
     */
    public FinalFastObjectDictionary(int initCapacity) {
        super(initCapacity);
    }

    /**
     * Instantiates a new Final fast object dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    public FinalFastObjectDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }
}
