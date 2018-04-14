package com.ss.rlib.common.util.array;

import com.ss.rlib.common.util.ArrayUtils;

/**
 * The interface Long array.
 */
public interface LongArray extends Iterable<Long> {

    /**
     * Add long array.
     *
     * @param element the element
     * @return the long array
     */
    LongArray add(long element);

    /**
     * Add all long array.
     *
     * @param array the array
     * @return the long array
     */
    LongArray addAll(long[] array);

    /**
     * Add all long array.
     *
     * @param array the array
     * @return the long array
     */
    LongArray addAll(LongArray array);

    /**
     * Array long [ ].
     *
     * @return the long [ ]
     */
    long[] array();

    /**
     * Clear long array.
     *
     * @return the long array
     */
    LongArray clear();

    /**
     * Contains boolean.
     *
     * @param element the element
     * @return the boolean
     */
    default boolean contains(final long element) {

        final long[] array = array();

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
    default boolean containsAll(final long[] array) {

        for (final long val : array) {
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
    default boolean containsAll(final LongArray array) {

        final long[] elements = array.array();

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
     * @param index the index
     * @return the boolean
     */
    boolean fastRemove(int index);

    /**
     * Fast remove boolean.
     *
     * @param element the element
     * @return the boolean
     */
    default boolean fastRemove(final long element) {

        final int index = indexOf(element);
        if (index > -1) fastRemove(index);

        return index > -1;
    }

    /**
     * First long.
     *
     * @return the long
     */
    long first();

    /**
     * Get long.
     *
     * @param index the index
     * @return the long
     */
    long get(int index);

    /**
     * Index of int.
     *
     * @param element the element
     * @return the int
     */
    default int indexOf(final long element) {

        final long[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    boolean isEmpty();

    @Override
    ArrayIterator<Long> iterator();

    /**
     * Last long.
     *
     * @return the long
     */
    long last();

    /**
     * Last index of int.
     *
     * @param element the element
     * @return the int
     */
    default int lastIndexOf(final long element) {

        final long[] array = array();

        int last = -1;

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) {
                last = i;
            }
        }

        return last;
    }

    /**
     * Poll long.
     *
     * @return the long
     */
    long poll();

    /**
     * Pop long.
     *
     * @return the long
     */
    long pop();

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
    default boolean removeAll(final LongArray target) {
        if (target.isEmpty()) return true;

        final long[] array = target.array();

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
    default boolean retainAll(final LongArray target) {

        final long[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (!target.contains(array[i])) {
                fastRemove(i--);
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
     * @param index the index
     * @return the boolean
     */
    boolean slowRemove(int index);

    /**
     * Slow remove boolean.
     *
     * @param element the element
     * @return the boolean
     */
    default boolean slowRemove(final long element) {

        final int index = indexOf(element);
        if (index > -1) slowRemove(index);

        return index > -1;
    }

    /**
     * Sort long array.
     *
     * @return the long array
     */
    LongArray sort();

    /**
     * To array long [ ].
     *
     * @param newArray the new array
     * @return the long [ ]
     */
    default long[] toArray(final long[] newArray) {

        final long[] array = array();

        if (newArray.length >= size()) {

            for (int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {
                newArray[j++] = array[i];
            }

            return newArray;
        }

        return ArrayUtils.copyOf(array, 0);
    }

    /**
     * Trim to size long array.
     *
     * @return the long array
     */
    LongArray trimToSize();

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
