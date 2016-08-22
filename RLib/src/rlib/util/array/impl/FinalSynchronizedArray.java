package rlib.util.array.impl;

/**
 * The final implementation of the {@link SynchronizedArray}.
 *
 * @author JavaSaBr.
 */
public final class FinalSynchronizedArray<E> extends SynchronizedArray<E> {

    public FinalSynchronizedArray(final Class<E> type) {
        super(type);
    }

    public FinalSynchronizedArray(final Class<E> type, final int size) {
        super(type, size);
    }
}
