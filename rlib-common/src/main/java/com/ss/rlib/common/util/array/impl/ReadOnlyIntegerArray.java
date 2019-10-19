package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.array.ArrayIterator;
import com.ss.rlib.common.util.array.IntegerArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * Simple implementation of read only integer array.
 *
 * @author JavaSaBr
 */
public class ReadOnlyIntegerArray implements IntegerArray {

    protected final int[] array;

    public ReadOnlyIntegerArray(int @NotNull [] array) {
        this.array = array;
    }

    @Override
    public final @NotNull int[] array() {
        return array;
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
        return array[index];
    }

    @Override
    public final @NotNull ArrayIterator<Integer> iterator() {
        return new ReadOnlyIterator();
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
    public final int size() {
        return array.length;
    }

    @Override
    public boolean equals(@Nullable Object another) {

        if (this == another) {
            return true;
        } else if (!(another instanceof IntegerArray)) {
            return false;
        }

        var array = (IntegerArray) another;
        var size = this.array.length;

        return size == array.size() &&
            Arrays.equals(this.array, 0, size, array.array(), 0, size);
    }

    @Override
    public int hashCode() {
        var result = Objects.hash(array.length);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    private final class ReadOnlyIterator implements ArrayIterator<Integer> {

        private int ordinal = 0;

        @Override
        public void fastRemove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return ordinal < array.length;
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
            throw new UnsupportedOperationException();
        }
    }
}
