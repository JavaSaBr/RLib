package rlib.util.array.impl;

/**
 * The final version of the {@link FastArray}.
 *
 * @author JavaSaBr
 */
public final class FinalFastArray<E> extends FastArray<E> {

    public FinalFastArray(final Class<E> type) {
        super(type);
    }

    public FinalFastArray(final Class<E> type, final int size) {
        super(type, size);
    }
}
