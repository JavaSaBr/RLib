package com.ss.rlib.common.util.array;

import org.jetbrains.annotations.NotNull;

public interface MutableIntegerArray extends IntegerArray {

    /**
     * Add a new number to this array.
     *
     * @param number the new integer.
     * @return this array.
     */
    @NotNull IntegerArray add(int number);

    /**
     * Add new numbers to this array.
     *
     * @param numbers the new numbers.
     * @return this array.
     */
    @NotNull IntegerArray addAll(int @NotNull [] numbers);

    /**
     * Add new numbers to this array.
     *
     * @param numbers the new numbers.
     * @return this array.
     */
    @NotNull IntegerArray addAll(@NotNull IntegerArray numbers);

    /**
     * Clear this array.
     *
     * @return this array.
     */
    @NotNull IntegerArray clear();

    /**
     * Remove the first equal number in array with putting last number to position of removed number.
     *
     * @param number the number to remove.
     * @return true if the number was removed.
     */
    default boolean fastRemove(int number) {

        var index = indexOf(number);

        if (index > -1){
            fastRemoveByIndex(index);
        }

        return index > -1;
    }

    /**
     * Remove a number by the index from this array with putting last number to position of removed number.
     *
     * @param index the index of number to remove.
     * @return true if the number was removed.
     */
    boolean fastRemoveByIndex(int index);

    /**
     * Get and remove the first number from this array.
     *
     * @return the first number.
     * @throws IllegalStateException if this array is empty.
     */
    int poll();

    /**
     * Get and remove the last number from this array.
     *
     * @return the last number.
     * @throws IllegalStateException if this array is empty.
     */
    int pop();

    /**
     * Remove the numbers from this array.
     *
     * @param numbers the array of numbers to remove.
     * @return true if at least one number was removed from this array.
     */
    default boolean removeAll(@NotNull IntegerArray numbers) {

        if (numbers.isEmpty()) {
            return false;
        }

        var array = numbers.array();
        var count = 0;

        for (int i = 0, length = numbers.size(); i < length; i++) {
            if(fastRemove(array[i])) {
                count++;
            }
        }

        return count > 0;
    }

    /**
     * Remove numbers from this array which is not exist in the target array.
     *
     * @param numbers the array of numbers.
     * @return true if at least one number was removed from this array.
     */
    default boolean retainAll(@NotNull IntegerArray numbers) {

        var array = array();
        var count = 0;

        for (int i = 0, length = size(); i < length; i++) {
            if (!numbers.contains(array[i])) {
                removeByIndex(i--);
                length--;
                count++;
            }
        }

        return count > 0;
    }

    /**
     * Remove the first equal number in array.
     *
     * @param number the number to remove.
     * @return true if the number was removed.
     */
    default boolean remove(int number) {

        var index = indexOf(number);

        if (index > -1) {
            removeByIndex(index);
        }

        return index > -1;
    }

    /**
     * Remove a number by the index from this array.
     *
     * @param index the index.
     * @return true if the number was removed.
     */
    boolean removeByIndex(int index);

    /**
     * Sort this array.
     *
     * @return this array.
     */
    @NotNull IntegerArray sort();

    /**
     * Resize wrapped array to the relevant size.
     *
     * @return this array.
     */
    @NotNull IntegerArray trimToSize();
}
