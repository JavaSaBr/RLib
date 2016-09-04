package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

import rlib.util.array.Array;

/**
 * The fast implementation of the array with checking on duplicates. This array is not threadsafe.
 *
 * @author JavaSaBr
 */
public class FastArraySet<E> extends FastArray<E> {

    private static final long serialVersionUID = 1L;

    public FastArraySet(final Class<E> type) {
        super(type);
    }

    public FastArraySet(final Class<E> type, final int size) {
        super(type, size);
    }

    @NotNull
    @Override
    public FastArray<E> add(@NotNull final E element) {
        return contains(element) ? this : super.add(element);
    }

    @Override
    protected void processAdd(final Array<? extends E> elements, final int selfSize, final int targetSize) {
        for (final E element : elements.array()) {
            if (element == null) break;
            if (!contains(element)) unsafeAdd(element);
        }
    }

    @Override
    protected void processAdd(final E[] elements, final int selfSize, final int targetSize) {
        for (final E element : elements) {
            if (element == null) break;
            if (!contains(element)) unsafeAdd(element);
        }
    }
}
