package com.ss.rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final implementation of the {@link ConcurrentStampedLockArray}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public final class FinalConcurrentStampedLockArray<E> extends ConcurrentStampedLockArray<E> {

    /**
     * Instantiates a new Final concurrent stamped lock array.
     *
     * @param type the type
     */
    public FinalConcurrentStampedLockArray(@NotNull final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Final concurrent stamped lock array.
     *
     * @param type the type
     * @param size the size
     */
    public FinalConcurrentStampedLockArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}
