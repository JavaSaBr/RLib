package com.ss.rlib.common.util.array.impl;

import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;

/**
 * The sorted implementation of the {@link FastArray}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class SortedArray<E extends Comparable<E>> extends FastArray<E> {

    private static final long serialVersionUID = 1L;

    public SortedArray(@NotNull Class<? super E> type) {
        super(type);
    }

    public SortedArray(@NotNull Class<? super E> type, int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull E element) {

        if (size == array.length) {
            array = ArrayUtils.copyOf(array, array.length * 3 / 2 + 1);
        }

        return unsafeAdd(element);
    }

    @Override
    protected void processAdd(@NotNull Array<? extends E> elements, int selfSize, int targetSize) {
        for (E element : elements.array()) {
            if (element == null) break;
            add(element);
        }
    }

    @Override
    protected void processAdd(@NotNull E[] elements, int selfSize, int targetSize) {
        for (E element : elements) {
            if (element == null) break;
            add(element);
        }
    }

    @Override
    public boolean unsafeAdd(@NotNull E element) {

        E[] array = array();

        for (int i = 0, length = array.length; i < length; i++) {

            E old = array[i];

            if (old == null) {
                array[i] = element;
                size++;
                return true;
            }

            if (element.compareTo(old) < 0) {

                size++;

                int numMoved = size - i - 1;

                System.arraycopy(array, i, array, i + 1, numMoved);

                array[i] = element;
                return true;
            }
        }

        return true;
    }
}