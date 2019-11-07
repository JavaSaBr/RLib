package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayIterator;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of the unsafeArray iterator.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
public class DefaultArrayIterator<E> implements ArrayIterator<E> {

    /**
     * The array for iteration.
     */
    private final @NotNull Array<E> array;

    /**
     * The unsafe array for directly access.
     */
    private final @NotNull E[] unsafeArray;

    /**
     * The current position in the array.
     */
    private int ordinal;

    public DefaultArrayIterator(@NotNull Array<E> array) {
        this.array = array;
        this.unsafeArray = array.array();
    }

    @Override
    public void fastRemove() {
        array.fastRemove(--ordinal);
    }

    @Override
    public boolean hasNext() {
        return ordinal < array.size();
    }

    @Override
    public int index() {
        return ordinal - 1;
    }

    @Override
    public @NotNull E next() {
        return ordinal >= unsafeArray.length ? null : unsafeArray[ordinal++];
    }

    @Override
    public void remove() {
        array.remove(--ordinal);
    }

    @Override
    public String toString() {
        return "ArrayIteratorImpl{" +
                "array=" + array +
                ", ordinal=" + ordinal +
                '}';
    }
}
