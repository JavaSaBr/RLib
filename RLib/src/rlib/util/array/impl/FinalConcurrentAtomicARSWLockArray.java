package rlib.util.array.impl;

/**
 * The final implementation of the {@link ConcurrentAtomicARSWLockArray}.
 *
 * @author JavaSaBr
 */
public final class FinalConcurrentAtomicARSWLockArray<E> extends ConcurrentAtomicARSWLockArray<E> {

    public FinalConcurrentAtomicARSWLockArray(final Class<E> type) {
        super(type);
    }

    public FinalConcurrentAtomicARSWLockArray(final Class<E> type, final int size) {
        super(type, size);
    }
}
