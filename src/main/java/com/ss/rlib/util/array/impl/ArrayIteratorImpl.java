package com.ss.rlib.util.array.impl;

import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayIterator;

/**
 * The implementation of the unsafeArray iterator.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ArrayIteratorImpl<E> implements ArrayIterator<E> {

    /**
     * The array for iteration.
     */
    private final Array<E> array;

    /**
     * The unsafe array for directly access.
     */
    private final E[] unsafeArray;

    /**
     * The current position in the array.
     */
    private int ordinal;

    /**
     * Instantiates a new Array iterator.
     *
     * @param array the array
     */
    public ArrayIteratorImpl(final Array<E> array) {
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
    public E next() {
        return ordinal >= unsafeArray.length ? null : unsafeArray[ordinal++];
    }

    @Override
    public void remove() {
        array.slowRemove(--ordinal);
    }

    @Override
    public String toString() {
        return "ArrayIteratorImpl{" +
                "array=" + array +
                ", ordinal=" + ordinal +
                '}';
    }
}
