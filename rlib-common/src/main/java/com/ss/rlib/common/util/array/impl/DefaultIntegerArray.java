package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.ArrayIterator;
import com.ss.rlib.common.util.array.IntegerArray;
import com.ss.rlib.common.util.array.MutableIntegerArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * Simple implementation of dynamic integer array.
 *
 * @author JavaSaBr
 */
public class DefaultIntegerArray implements MutableIntegerArray {

    protected int[] array;
    protected int size;

    public DefaultIntegerArray() {
        this(10);
    }

    public DefaultIntegerArray(int size) {
        this.array = new int[size];
        this.size = 0;
    }

    public DefaultIntegerArray(int @NotNull [] numbers) {
        this.array = numbers;
        this.size = numbers.length;
    }

    @Override
    public @NotNull DefaultIntegerArray add(int number) {

        if (size == array.length) {
            array = ArrayUtils.copyOf(array, Math.max(array.length >> 1, 1));
        }

        array[size++] = number;

        return this;
    }

    @Override
    public @NotNull DefaultIntegerArray addAll(int @NotNull [] numbers) {

        if (numbers.length < 1) {
            return this;
        }

        var current = array.length;
        var diff = size() + numbers.length - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        for (var value : numbers) {
            add(value);
        }

        return this;
    }

    @Override
    public final @NotNull DefaultIntegerArray addAll(@NotNull IntegerArray numbers) {

        if (numbers.isEmpty()) {
            return this;
        }

        var current = array.length;
        var diff = size() + numbers.size() - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        var array = numbers.array();

        for (int i = 0, length = numbers.size(); i < length; i++) {
            add(array[i]);
        }

        return this;
    }

    @Override
    public final @NotNull int[] array() {
        return array;
    }

    @Override
    public final @NotNull DefaultIntegerArray clear() {
        size = 0;
        return this;
    }

    @Override
    public final boolean fastRemoveByIndex(int index) {

        if (index < 0 || size < 1 || index >= size) {
            return false;
        }

        var array = array();

        size -= 1;

        array[index] = array[size];
        array[size] = 0;

        return true;
    }

    @Override
    public int first() {
        if (isEmpty()) {
            throw new IllegalStateException("Array is empty.");
        } else {
            return array[0];
        }
    }

    @Override
    public final int get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(index);
        } else {
            return array[index];
        }
    }

    @Override
    public final @NotNull ArrayIterator<Integer> iterator() {
        return new DefaultIterator();
    }

    @Override
    public final int last() {
        if (isEmpty()) {
            throw new IllegalStateException("Array is empty.");
        } else {
            return array.length < 1 ? -1 : array[array.length - 1];
        }
    }

    @Override
    public final int poll() {
        var val = first();
        return removeByIndex(0) ? val : -1;
    }

    @Override
    public final int pop() {
        var last = last();
        return fastRemoveByIndex(size - 1) ? last : -1;
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final boolean removeByIndex(int index) {

        if (index < 0 || size < 1) {
            return false;
        }

        var array = array();
        var numMoved = size - index - 1;

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        array[--size] = 0;
        return true;
    }

    @Override
    public final @NotNull DefaultIntegerArray sort() {
        ArrayUtils.sort(array, 0, size);
        return this;
    }

    @Override
    public final @NotNull DefaultIntegerArray trimToSize() {

        var array = array();

        if (size == array.length) {
            return this;
        }

        this.array = ArrayUtils.copyOfRange(array, 0, size);
        return this;
    }

    @Override
    public boolean equals(@Nullable Object another) {

        if (this == another) {
            return true;
        } else if (!(another instanceof IntegerArray)) {
            return false;
        }

        var array = (IntegerArray) another;

        return size == array.size() &&
            Arrays.equals(this.array, 0, size, array.array(), 0, size);
    }

    @Override
    public int hashCode() {
        var result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    private final class DefaultIterator implements ArrayIterator<Integer> {

        private int ordinal = 0;

        @Override
        public void fastRemove() {
            DefaultIntegerArray.this.fastRemove(--ordinal);
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
        public @NotNull Integer next() {
            return array[ordinal++];
        }

        @Override
        public void remove() {
            DefaultIntegerArray.this.fastRemove(--ordinal);
        }
    }
}
