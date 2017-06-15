package com.ss.rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final implementation of the {@link SortedArray}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public final class FinalSortedArray<E extends Comparable<E>> extends SortedArray<E> {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Final sorted array.
     *
     * @param type the type
     */
    public FinalSortedArray(@NotNull final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Final sorted array.
     *
     * @param type the type
     * @param size the size
     */
    public FinalSortedArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}