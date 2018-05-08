package com.ss.rlib.common.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final implementation of the {@link SynchronizedArray}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr.
 */
public final class FinalSynchronizedArray<E> extends SynchronizedArray<E> {

    /**
     * Instantiates a new Final synchronized array.
     *
     * @param type the type
     */
    public FinalSynchronizedArray(@NotNull final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Final synchronized array.
     *
     * @param type the type
     * @param size the size
     */
    public FinalSynchronizedArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}
