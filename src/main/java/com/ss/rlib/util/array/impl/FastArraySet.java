package com.ss.rlib.util.array.impl;

import com.ss.rlib.util.array.Array;
import org.jetbrains.annotations.NotNull;

/**
 * The fast implementation of the array with checking on duplicates. This array is not threadsafe.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FastArraySet<E> extends FastArray<E> {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Fast array set.
     *
     * @param type the type
     */
    public FastArraySet(final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Fast array set.
     *
     * @param type the type
     * @param size the size
     */
    public FastArraySet(final Class<E> type, final int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull final E element) {
        return !contains(element) && super.add(element);
    }

    @Override
    protected void processAdd(@NotNull final Array<? extends E> elements, final int selfSize, final int targetSize) {
        for (final E element : elements.array()) {
            if (element == null) break;
            if (!contains(element)) unsafeAdd(element);
        }
    }

    @Override
    protected void processAdd(@NotNull final E[] elements, final int selfSize, final int targetSize) {
        for (final E element : elements) {
            if (element == null) break;
            if (!contains(element)) unsafeAdd(element);
        }
    }
}
