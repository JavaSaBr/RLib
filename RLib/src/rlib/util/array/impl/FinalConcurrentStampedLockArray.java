package rlib.util.array.impl;

/**
 * The final implementation of the {@link ConcurrentStampedLockArray}.
 *
 * @author JavaSaBr
 */
public final class FinalConcurrentStampedLockArray<E> extends ConcurrentStampedLockArray<E> {

    public FinalConcurrentStampedLockArray(final Class<E> type) {
        super(type);
    }

    public FinalConcurrentStampedLockArray(final Class<E> type, final int size) {
        super(type, size);
    }
}
