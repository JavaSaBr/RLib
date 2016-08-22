package rlib.util.array.impl;

/**
 * The final implementation of the {@link ConcurrentPrimitiveAtomicARSWLockArray}.
 *
 * @author JavaSaBr
 */
public final class FinalConcurrentPrimitiveAtomicARSWLockArray<E> extends ConcurrentPrimitiveAtomicARSWLockArray<E> {

    public FinalConcurrentPrimitiveAtomicARSWLockArray(final Class<E> type) {
        super(type);
    }

    public FinalConcurrentPrimitiveAtomicARSWLockArray(final Class<E> type, final int size) {
        super(type, size);
    }
}
