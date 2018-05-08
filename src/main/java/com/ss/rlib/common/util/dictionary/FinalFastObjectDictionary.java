package com.ss.rlib.common.util.dictionary;

/**
 * The final implementation of {@link FastObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class FinalFastObjectDictionary<K, V> extends FastObjectDictionary<K, V> {

    public FinalFastObjectDictionary() {
    }

    public FinalFastObjectDictionary(float loadFactor) {
        super(loadFactor);
    }

    public FinalFastObjectDictionary(int initCapacity) {
        super(initCapacity);
    }

    public FinalFastObjectDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }
}
