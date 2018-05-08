package com.ss.rlib.common.util.random;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a random.
 *
 * @author JavaSaBr
 */
public interface Random {

    /**
     * Fill an array random values.
     *
     * @param array  the array.
     * @param offset the offset.
     * @param length the length.
     */
    void byteArray(@NotNull byte[] array, int offset, int length);

    /**
     * Try to get success.
     *
     * @param chance the chance from 0 to 100.
     * @return true if the chance was successful.
     */
    boolean chance(float chance);

    /**
     * Try to get success.
     *
     * @param chance the chance from 0 to 100.
     * @return true if the chance was successful.
     */
    boolean chance(int chance);

    /**
     * Get a next random float value.
     *
     * @return the next value from 0.0 to 1.0.
     */
    float nextFloat();

    /**
     * Get a next random int value.
     *
     * @return the next value from int_min to int_max.
     */
    int nextInt();

    /**
     * Get a next random int value.
     *
     * @param max the max value.
     * @return the next value [0, max].
     */
    int nextInt(int max);

    /**
     * Get a next random int value.
     *
     * @param min the min value.
     * @param max the max value.
     * @return the next value [min, max].
     */
    int nextInt(int min, int max);

    /**
     * Get a next random long value.
     *
     * @param min the min value.
     * @param max the max value.
     * @return the next value [min, max].
     */
    long nextLong(long min, long max);
}
