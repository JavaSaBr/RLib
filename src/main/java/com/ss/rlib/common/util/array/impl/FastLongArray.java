package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.array.ArrayIterator;
import com.ss.rlib.common.util.array.LongArray;
import com.ss.rlib.common.util.ArrayUtils;

/**
 * Реализация не потокобезопасного динамического массива примитивов long.
 *
 * @author JavaSaBr
 */
public class FastLongArray implements LongArray {

    /**
     * Массив элементов.
     */
    protected long[] array;

    /**
     * Кол-во элементов в колекции.
     */
    protected int size;

    /**
     * Instantiates a new Fast long array.
     */
    public FastLongArray() {
        this(10);
    }

    /**
     * Instantiates a new Fast long array.
     *
     * @param size the size
     */
    public FastLongArray(final int size) {
        this.array = new long[size];
        this.size = 0;
    }

    @Override
    public FastLongArray add(final long element) {

        if (size == array.length) {
            array = ArrayUtils.copyOf(array, Math.max(array.length >> 1, 1));
        }

        array[size++] = element;
        return this;
    }

    @Override
    public final FastLongArray addAll(final long[] elements) {
        if (elements == null || elements.length < 1) return this;

        final int current = array.length;
        final int diff = size() + elements.length - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        for (final long element : elements) {
            add(element);
        }

        return this;
    }

    @Override
    public final FastLongArray addAll(final LongArray elements) {
        if (elements == null || elements.isEmpty()) return this;

        final int current = array.length;
        final int diff = size() + elements.size() - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        final long[] array = elements.array();

        for (int i = 0, length = elements.size(); i < length; i++) {
            add(array[i]);
        }

        return this;
    }

    @Override
    public final long[] array() {
        return array;
    }

    @Override
    public final FastLongArray clear() {
        size = 0;
        return this;
    }

    @Override
    public final boolean fastRemove(final int index) {

        if (index < 0 || size < 1 || index >= size) {
            return false;
        }

        final long[] array = array();

        size -= 1;

        array[index] = array[size];
        array[size] = 0;

        return true;
    }

    @Override
    public final long first() {
        return size < 1 ? -1 : array[0];
    }

    @Override
    public final long get(final int index) {
        return array[index];
    }

    @Override
    public final int indexOf(final long element) {

        final long[] array = array();

        for (int i = 0, length = size; i < length; i++) {
            final long val = array[i];
            if (element == val) return i;
        }

        return -1;
    }

    @Override
    public final boolean isEmpty() {
        return size < 1;
    }

    @Override
    public final ArrayIterator<Long> iterator() {
        return new FastIterator();
    }

    @Override
    public final long last() {
        if (size < 1) return 0;
        return array[size - 1];
    }

    @Override
    public final long poll() {
        final long val = first();
        return slowRemove(0) ? val : -1;
    }

    @Override
    public final long pop() {
        final long last = last();
        return fastRemove(size - 1) ? last : -1;
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final boolean slowRemove(final int index) {
        if (index < 0 || size < 1) return false;

        final long[] array = array();
        final int numMoved = size - index - 1;

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        size -= 1;
        array[size] = 0;

        return true;
    }

    @Override
    public final FastLongArray sort() {
        ArrayUtils.sort(array, 0, size);
        return this;
    }

    @Override
    public String toString() {
        return ArrayUtils.toString(this);
    }

    @Override
    public final FastLongArray trimToSize() {

        long[] array = array();

        if (size == array.length) {
            return this;
        }

        this.array = ArrayUtils.copyOfRange(array, 0, size);
        return this;
    }

    /**
     * Быстрый итератор массива.
     *
     * @author JavaSaBr
     */
    private final class FastIterator implements ArrayIterator<Long> {

        /**
         * текущая позиция в массиве
         */
        private int ordinal;

        /**
         * Instantiates a new Fast iterator.
         */
        public FastIterator() {
            ordinal = 0;
        }

        @Override
        public void fastRemove() {
            FastLongArray.this.fastRemove(--ordinal);
        }

        @Override
        public boolean hasNext() {
            return ordinal < size;
        }

        @Override
        public int index() {
            return ordinal - 1;
        }

        @Override
        public Long next() {
            return array[ordinal++];
        }

        @Override
        public void remove() {
            FastLongArray.this.fastRemove(--ordinal);
        }
    }
}