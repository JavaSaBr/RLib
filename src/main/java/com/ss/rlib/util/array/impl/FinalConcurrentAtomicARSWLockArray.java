package com.ss.rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final implementation of the {@link ConcurrentAtomicARSWLockArray}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public final class FinalConcurrentAtomicARSWLockArray<E> extends ConcurrentAtomicARSWLockArray<E> {

    /**
     * Instantiates a new Final concurrent atomic arsw lock array.
     *
     * @param type the type
     */
    public FinalConcurrentAtomicARSWLockArray(@NotNull final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Final concurrent atomic arsw lock array.
     *
     * @param type the type
     * @param size the size
     */
    public FinalConcurrentAtomicARSWLockArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}
