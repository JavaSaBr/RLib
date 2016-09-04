package rlib.util.dictionary;

/**
 * The final implementation of {@link FastObjectDictionary}.
 *
 * @author Ronn
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
