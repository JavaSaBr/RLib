package com.ss.rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final version of the {@link FastArray}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public final class FinalFastArray<E> extends FastArray<E> {

    /**
     * Instantiates a new Final fast array.
     *
     * @param type the type
     */
    public FinalFastArray(@NotNull final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Final fast array.
     *
     * @param type the type
     * @param size the size
     */
    public FinalFastArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}
