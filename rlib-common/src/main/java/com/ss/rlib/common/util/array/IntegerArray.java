package com.ss.rlib.common.util.array;

import com.ss.rlib.common.util.ArrayUtils;

/**
 * The interface Integer array.
 */
public interface IntegerArray extends Iterable<Integer> {

    /**
     * Add integer array.
     *
     * @param element the element
     * @return the integer array
     */
    IntegerArray add(int element);

    /**
     * Add all integer array.
     *
     * @param array the array
     * @return the integer array
     */
    IntegerArray addAll(int[] array);

    /**
     * Add all integer array.
     *
     * @param array the array
     * @return the integer array
     */
    IntegerArray addAll(IntegerArray array);

    /**
     * Array int [ ].
     *
     * @return the int [ ]
     */
    int[] array();

    /**
     * Clear integer array.
     *
     * @return the integer array
     */
    IntegerArray clear();

    /**
     * Contains boolean.
     *
     * @param element the element
     * @return the boolean
     */
    default boolean contains(final int element) {

        final int[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (array[i] == element) return true;
        }

        return false;
    }

    /**
     * Contains all boolean.
     *
     * @param array the array
     * @return the boolean
     */
    default boolean containsAll(final int[] array) {

        for (final int val : array) {
            if (!contains(val)) return false;
        }

        return true;
    }

    /**
     * Contains all boolean.
     *
     * @param array the array
     * @return the boolean
     */
    default boolean containsAll(final IntegerArray array) {

        final int[] elements = array.array();

        for (int i = 0, length = array.size(); i < length; i++) {
            if (!contains(elements[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Fast remove boolean.
     *
     * @param element the element
     * @return the boolean
     */
    default boolean fastRemove(final int element) {

        final int index = indexOf(element);
        if (index > -1) fastRemoveByIndex(index);

        return index > -1;
    }

    /**
     * Fast remove by index boolean.
     *
     * @param index the index
     * @return the boolean
     */
    boolean fastRemoveByIndex(int index);

    /**
     * First int.
     *
     * @return the int
     */
    int first();

    /**
     * Get int.
     *
     * @param index the index
     * @return the int
     */
    int get(int index);

    /**
     * Index of int.
     *
     * @param element the element
     * @return the int
     */
    default int indexOf(final int element) {

        final int[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) return i;
        }

        return -1;
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    default boolean isEmpty() {
        return size() < 1;
    }

    @Override
    ArrayIterator<Integer> iterator();

    /**
     * Last int.
     *
     * @return the int
     */
    int last();

    /**
     * Last index of int.
     *
     * @param element the element
     * @return the int
     */
    default int lastIndexOf(final int element) {

        final int[] array = array();

        int last = -1;

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) last = i;
        }

        return last;
    }

    /**
     * Poll int.
     *
     * @return the int
     */
    int poll();

    /**
     * Pop int.
     *
     * @return the int
     */
    int pop();

    /**
     * Read lock.
     */
    default void readLock() {
    }

    /**
     * Read unlock.
     */
    default void readUnlock() {
    }

    /**
     * Remove all boolean.
     *
     * @param target the target
     * @return the boolean
     */
    default boolean removeAll(final IntegerArray target) {
        if (target.isEmpty()) return true;

        final int[] array = target.array();

        for (int i = 0, length = target.size(); i < length; i++) {
            fastRemove(array[i]);
        }

        return true;
    }

    /**
     * Retain all boolean.
     *
     * @param target the target
     * @return the boolean
     */
    default boolean retainAll(final IntegerArray target) {

        final int[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (!target.contains(array[i])) {
                fastRemoveByIndex(i--);
                length--;
            }
        }

        return true;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    int size();

    /**
     * Slow remove boolean.
     *
     * @param element the element
     * @return the boolean
     */
    default boolean slowRemove(final int element) {

        final int index = indexOf(element);
        if (index > -1) slowRemoveByIndex(index);

        return index > -1;
    }

    /**
     * Slow remove by index boolean.
     *
     * @param index the index
     * @return the boolean
     */
    boolean slowRemoveByIndex(int index);

    /**
     * Sort integer array.
     *
     * @return the integer array
     */
    IntegerArray sort();

    /**
     * To array int [ ].
     *
     * @param newArray the new array
     * @return the int [ ]
     */
    default int[] toArray(final int[] newArray) {

        final int[] array = array();

        if (newArray.length >= size()) {

            for (int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {
                newArray[j++] = array[i];
            }

            return newArray;
        }

        return ArrayUtils.copyOf(array, 0);
    }

    /**
     * Trim to size integer array.
     *
     * @return the integer array
     */
    IntegerArray trimToSize();

    /**
     * Write lock.
     */
    default void writeLock() {
    }

    /**
     * Write unlock.
     */
    default void writeUnlock() {
    }
}
