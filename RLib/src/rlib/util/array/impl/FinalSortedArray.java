package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

/**
 * The final implementation of the {@link SortedArray}.
 *
 * @author JavaSaBr
 */
public final class FinalSortedArray<E extends Comparable<E>> extends SortedArray<E> {

    private static final long serialVersionUID = 1L;

    public FinalSortedArray(@NotNull final Class<E> type) {
        super(type);
    }

    public FinalSortedArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }
}