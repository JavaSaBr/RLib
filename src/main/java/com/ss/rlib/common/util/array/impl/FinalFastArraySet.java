package com.ss.rlib.common.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final version of the {@link FastArraySet}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public final class FinalFastArraySet<E> extends FastArraySet<E> {

    /**
     * Instantiates a new Final fast array set.
     *
     * @param type the type
     */
    public FinalFastArraySet(@NotNull final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Final fast array set.
     *
     * @param type the type
     * @param size the size
     */
    public FinalFastArraySet(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}
