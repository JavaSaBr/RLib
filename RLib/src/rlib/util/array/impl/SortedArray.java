package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

import rlib.util.ArrayUtils;
import rlib.util.array.Array;

/**
 * The sorted implementation of the {@link FastArray}.
 *
 * @author JavaSaBr
 */
public class SortedArray<E extends Comparable<E>> extends FastArray<E> {

    private static final long serialVersionUID = 1L;

    public SortedArray(final Class<E> type) {
        super(type);
    }

    public SortedArray(final Class<E> type, final int size) {
        super(type, size);
    }

    @NotNull
    @Override
    public SortedArray<E> add(@NotNull final E element) {

        if (size == array.length) {
            array = ArrayUtils.copyOf(array, array.length * 3 / 2 + 1);
        }

        final E[] array = array();

        for (int i = 0, length = array.length; i < length; i++) {

            final E old = array[i];

            if (old == null) {
                array[i] = element;
                size++;
                return this;
            }

            if (element.compareTo(old) < 0) {
                size++;

                final int numMoved = size - i - 1;

                System.arraycopy(array, i, array, i + 1, numMoved);

                array[i] = element;
                return this;
            }
        }

        return this;
    }

    @Override
    protected void processAdd(final Array<? extends E> elements, final int selfSize, final int targetSize) {
        for (final E element : elements.array()) {
            if (element == null) break;
            add(element);
        }
    }

    @Override
    protected void processAdd(final E[] elements, final int selfSize, final int targetSize) {
        for (final E element : elements) {
            if (element == null) break;
            add(element);
        }
    }

    @Override
    public FastArray<E> unsafeAdd(@NotNull final E element) {

        final E[] array = array();

        for (int i = 0, length = array.length; i < length; i++) {

            final E old = array[i];

            if (old == null) {
                array[i] = element;
                size++;
                return this;
            }

            if (element.compareTo(old) < 0) {
                size++;

                final int numMoved = size - i - 1;

                System.arraycopy(array, i, array, i + 1, numMoved);

                array[i] = element;
                return this;
            }
        }

        return this;
    }
}