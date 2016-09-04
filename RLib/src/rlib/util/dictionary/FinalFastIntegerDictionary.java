package rlib.util.dictionary;

/**
 * The final implementation of {@link FastIntegerDictionary}.
 *
 * @author Ronn
 */
public final class FinalFastIntegerDictionary<V> extends FastIntegerDictionary<V> {

    public FinalFastIntegerDictionary() {
    }

    public FinalFastIntegerDictionary(float loadFactor) {
        super(loadFactor);
    }

    public FinalFastIntegerDictionary(int initCapacity) {
        super(initCapacity);
    }

    public FinalFastIntegerDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }
}
