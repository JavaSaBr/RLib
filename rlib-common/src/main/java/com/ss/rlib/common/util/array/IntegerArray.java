package com.ss.rlib.common.util.array;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.impl.ReadOnlyIntegerArray;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * The interface of dynamic integer array.
 */
public interface IntegerArray extends Iterable<Integer> {

    @NotNull IntegerArray EMPTY = new ReadOnlyIntegerArray(ArrayUtils.EMPTY_INT_ARRAY);

    static @NotNull IntegerArray of(int value) {
        return new ReadOnlyIntegerArray(ArrayFactory.toIntArray(value));
    }

    static @NotNull IntegerArray of(int v1, int v2) {
        return new ReadOnlyIntegerArray(ArrayFactory.toIntArray(v1, v2));
    }

    static @NotNull IntegerArray of(int v1, int v2, int v3) {
        return new ReadOnlyIntegerArray(ArrayFactory.toIntArray(v1, v2, v3));
    }

    static @NotNull IntegerArray of(int... values) {
        return new ReadOnlyIntegerArray(ArrayFactory.toIntArray(values));
    }

    /**
     * Return the wrapped int array.
     * Don't change this array, please.
     *
     * @return the wrapped int array.
     */
    int @NotNull [] array();

    default boolean contains(int element) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (array[i] == element) {
                return true;
            }
        }

        return false;
    }

    default boolean containsAll(int @NotNull [] array) {

        for (int val : array) {
            if (!contains(val)) {
                return false;
            }
        }

        return true;
    }

    default boolean containsAll(@NotNull IntegerArray array) {

        var elements = array.array();

        for (int i = 0, length = array.size(); i < length; i++) {
            if (!contains(elements[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the first number.
     *
     * @return the first number.
     * @throws IllegalStateException if this array is empty.
     */
    int first();

    /**
     * Get a number by the index.
     *
     * @param index the index.
     * @return the number.
     */
    int get(int index);

    /**
     * Find index of the first equal number in this array.
     *
     * @param element the checked number.
     * @return the found index or -1.
     */
    default int indexOf(int element) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Return true if this array is empty.
     *
     * @return true if this array is empty.
     */
    default boolean isEmpty() {
        return size() < 1;
    }

    @Override
    @NotNull ArrayIterator<Integer> iterator();

    @Override
    default void forEach(@NotNull Consumer<? super Integer> consumer) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(array[i]);
        }
    }

    default void forEachInt(@NotNull IntConsumer consumer) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(array[i]);
        }
    }

    /**
     * Get the last number in this array.
     *
     * @return the last number.
     * @throws IllegalStateException if this array is empty.
     */
    int last();

    /**
     * Find index of the last equal number in this array.
     *
     * @param element the checked number.
     * @return the found index or -1.
     */
    default int lastIndexOf(int element) {

        var array = array();
        var last = -1;

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) {
                last = i;
            }
        }

        return last;
    }

    /**
     * Get the current count of numbers in this array.
     *
     * @return the current count of numbers in this array.
     */
    int size();

    /**
     * Create new array from this array.
     *
     * @return the array with data from this array.
     */
    default int @NotNull [] toArray() {
        return ArrayUtils.copyOfRange(array(), 0, size());
    }

    /**
     * Copy or create new array from this array.
     *
     * @param newArray the new array.
     * @return the array with data from this array.
     */
    default int @NotNull [] toArray(int @NotNull [] newArray) {

        var array = array();

        if (newArray.length >= size()) {

            for (int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {
                newArray[j++] = array[i];
            }

            return newArray;
        }

        return ArrayUtils.copyOfRange(array(), 0, size());
    }

    default @NotNull IntStream stream() {
        return Arrays.stream(array(), 0, size());
    }
}
