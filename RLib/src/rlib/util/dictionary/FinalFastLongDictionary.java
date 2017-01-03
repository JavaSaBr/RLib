package rlib.util.dictionary;

/**
 * The final implementation of {@link FinalFastLongDictionary}.
 *
 * @author Ronn
 */
public final class FinalFastLongDictionary<V> extends FastLongDictionary<V> {

    public FinalFastLongDictionary() {
    }

    public FinalFastLongDictionary(final float loadFactor) {
        super(loadFactor);
    }

    public FinalFastLongDictionary(final int initCapacity) {
        super(initCapacity);
    }

    public FinalFastLongDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }
}
