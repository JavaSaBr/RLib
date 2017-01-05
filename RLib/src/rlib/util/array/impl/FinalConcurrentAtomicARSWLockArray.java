package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final implementation of the {@link ConcurrentAtomicARSWLockArray}.
 *
 * @author JavaSaBr
 */
public final class FinalConcurrentAtomicARSWLockArray<E> extends ConcurrentAtomicARSWLockArray<E> {

    public FinalConcurrentAtomicARSWLockArray(@NotNull final Class<E> type) {
        super(type);
    }

    public FinalConcurrentAtomicARSWLockArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}
