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

    public SortedArray(@NotNull final Class<E> type) {
        super(type);
    }

    public SortedArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull final E element) {

        if (size == array.length) {
            array = ArrayUtils.copyOf(array, array.length * 3 / 2 + 1);
        }

        final E[] array = array();

        for (int i = 0, length = array.length; i < length; i++) {

            final E old = array[i];

            if (old == null) {
                array[i] = element;
                size++;
                return true;
            }

            if (element.compareTo(old) < 0) {
                size++;

                final int numMoved = size - i - 1;

                System.arraycopy(array, i, array, i + 1, numMoved);

                array[i] = element;
                return true;
            }
        }

        return true;
    }

    @Override
    protected void processAdd(@NotNull final Array<? extends E> elements, final int selfSize, final int targetSize) {
        for (final E element : elements.array()) {
            if (element == null) break;
            add(element);
        }
    }

    @Override
    protected void processAdd(@NotNull final E[] elements, final int selfSize, final int targetSize) {
        for (final E element : elements) {
            if (element == null) break;
            add(element);
        }
    }

    @Override
    public boolean unsafeAdd(@NotNull final E element) {

        final E[] array = array();

        for (int i = 0, length = array.length; i < length; i++) {

            final E old = array[i];

            if (old == null) {
                array[i] = element;
                size++;
                return true;
            }

            if (element.compareTo(old) < 0) {
                size++;

                final int numMoved = size - i - 1;

                System.arraycopy(array, i, array, i + 1, numMoved);

                array[i] = element;
                return true;
            }
        }

        return true;
    }
}