package rlib.util.array.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayIterator;

/**
 * Реализация итератора динамического массива.
 *
 * @author Ronn
 */
public class ArrayIteratorImpl<E> implements ArrayIterator<E> {

    /**
     * Итерируемая коллекция.
     */
    private final Array<E> collection;

    /**
     * Итерируемый массив.
     */
    private final E[] array;

    /**
     * Текущая позиция в массиве.
     */
    private int ordinal;

    public ArrayIteratorImpl(final Array<E> collection) {
        this.collection = collection;
        this.array = collection.array();
    }

    @Override
    public void fastRemove() {
        collection.fastRemove(--ordinal);
    }

    @Override
    public boolean hasNext() {
        return ordinal < collection.size();
    }

    @Override
    public int index() {
        return ordinal - 1;
    }

    @Override
    public E next() {
        return ordinal >= array.length ? null : array[ordinal++];
    }

    @Override
    public void remove() {
        collection.fastRemove(--ordinal);
    }

    @Override
    public void slowRemove() {
        collection.fastRemove(--ordinal);
    }
}
