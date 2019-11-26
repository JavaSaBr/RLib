package com.ss.rlib.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public final class NumberUtils {

    /**
     * Get a short value from a byte array.
     *
     * @param bytes  the byte array.
     * @param offset the offset.
     * @return the short value.
     */
    public static short getShort(@NotNull byte[] bytes, int offset) {
        return (short) (bytes[offset + 1] << 8 | bytes[offset] & 0xff);
    }

    /**
     * Return true if a string is not null and can be converted to a long.
     *
     * @param string the string to convert.
     * @return if the string is not null and can be converted to a long.
     * @since 9.3.0
     */
    public static boolean isLong(@Nullable String string) {

        if (string == null) {
            return false;
        } else {
            try {
                Long.parseLong(string);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    /**
     * Convert a string to int object or null if this string is null or not a number.
     *
     * @param string the string to convert.
     * @return the int object or null.
     * @since 9.4.0
     */
    public static @Nullable Integer safeToInt(@Nullable String string) {

        if (string == null) {
            return null;
        } else {
            try {
                return Integer.valueOf(string);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    /**
     * Convert a string to long object or null if this string is null or not a number.
     *
     * @param string the string to convert.
     * @return the long object or null.
     * @since 9.3.0
     */
    public static @Nullable Long safeToLong(@Nullable String string) {

        if (string == null) {
            return null;
        } else {
            try {
                return Long.valueOf(string);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    /**
     * Convert a string to long object.
     *
     * @param string the string to convert.
     * @return the optional of long object.
     * @since 9.3.0
     */
    public static @NotNull Optional<Long> toOptionalLong(@Nullable String string) {

        if (string == null) {
            return Optional.empty();
        } else {
            try {
                return Optional.of(Long.valueOf(string));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
    }

    /**
     * Set a bit in a number by a pos to 1.
     *
     * @param value the byte.
     * @param pos   the bit position.
     * @return the update number.
     * @since 9.4.0
     */
    public static int setBit(int value, int pos) {
        return value | (1 << pos);
    }

    /**
     * Set a bit in a number by a pos to 0.
     *
     * @param value the byte.
     * @param pos   the bit position.
     * @return the update number.
     * @since 9.4.0
     */
    public static int unsetBit(int value, int pos) {
        return value & ~(1 << pos);
    }

    /**
     * Return true if bit by pos in a byte is 1.
     *
     * @param value the byte.
     * @param pos   the bit position.
     * @return true if the bit is 1.
     * @since 9.4.0
     */
    public static boolean isSetBit(int value, int pos) {
        return (value & (1L << pos)) != 0;
    }

    /**
     * Return true if bit by pos in a byte is 0.
     *
     * @param value the byte.
     * @param pos   the bit position.
     * @return true if the bit is 0.
     * @since 9.4.0
     */
    public static boolean isNotSetBit(int value, int pos) {
        return (value & (1L << pos)) == 0;
    }

    /**
     * Set last high 4 bits to a byte.
     *
     * @param value the byte value.
     * @return the result value with updating last high 4 bits.
     * @since 9.4.0
     */
    public static int setHighByteBits(int value, int highBits) {
        return value | highBits << 4;
    }

    /**
     * Get last high 4 bits from a byte.
     *
     * @param value the byte value.
     * @return the value of last 4 high bits.
     * @since 9.4.0
     */
    public static byte getHighByteBits(int value) {
        return (byte) (value >> 4);
    }

    /**
     * Set first low 4 bits to a byte.
     *
     * @param value the byte value.
     * @return the result value with updating first low 4 bits.
     * @since 9.4.0
     */
    public static int setLowByteBits(int value, int lowBits) {
        return value | lowBits & 0x0F;
    }

    /**
     * Get first low 4 bits from a byte.
     *
     * @param value the byte value.
     * @return the value of last 4 low bits.
     * @since 9.4.0
     */
    public static byte getLowByteBits(int value) {
        return (byte) (value & 0x0F);
    }

    /**
     * Validate a number and throw an exception when the number is not valid.
     *
     * @param value the value.
     * @param min   the min number.
     * @param max   the max number.
     * @return the passed number.
     * @since 9.5.0
     */
    public static int validate(int value, int min, int max) {
        return validate(value, min, max, IllegalArgumentException::new);
    }

    /**
     * Validate a number and throw an exception when the number is not valid.
     *
     * @param value            the value.
     * @param min              the min number.
     * @param max              the max number.
     * @param exceptionFactory the exception factory.
     * @return the passed number.
     * @since 9.5.0
     */
    public static int validate(
        int value,
        int min,
        int max,
        @NotNull Function<@NotNull String, @NotNull RuntimeException> exceptionFactory
    ) {
        if (value < min || value > max) {
            throw exceptionFactory.apply("Invalid value: " + value + " when should be " + min + " < v < " + max);
        } else {
            return value;
        }
    }

    /**
     * Validate a number and throw an exception when the number is not valid.
     *
     * @param value the validated value.
     * @param min   the min number.
     * @param max   the max number.
     * @return the passed number.
     * @since 9.5.0
     */
    public static long validate(long value, long min, long max) {
        return validate(value, min, max, IllegalArgumentException::new);
    }

    /**
     * Validate a number and throw an exception when the number is not valid.
     *
     * @param value            the validated value.
     * @param min              the min number.
     * @param max              the max number.
     * @param exceptionFactory the exception factory.
     * @return the passed number.
     * @since 9.5.0
     */
    public static long validate(
        long value,
        long min,
        long max,
        @NotNull Function<@NotNull String, @NotNull RuntimeException> exceptionFactory
    ) {
        if (value < min || value > max) {
            throw exceptionFactory.apply("Invalid value: " + value + " when should be " + min + " < v < " + max);
        } else {
            return value;
        }
    }

    /**
     * Convert a number to boolean which should be only 0 or 1 or throw an exception.
     *
     * @param value the validated value.
     * @return the validated value.
     */
    public static boolean toBoolean(int value) {
        return toBoolean(value, IllegalArgumentException::new);
    }

    /**
     * Convert a number to boolean which should be only 0 or 1 or throw an exception.
     *
     * @param value the validated value.
     * @return the validated value.
     */
    public static boolean toBoolean(long value) {
        return toBoolean(value, IllegalArgumentException::new);
    }

    /**
     * Convert a number to boolean which should be only 0 or 1 or throw an exception.
     *
     * @param value            the validated value.
     * @param exceptionFactory the exception factory.
     * @return the validated value.
     */
    public static boolean toBoolean(
        int value,
        @NotNull Function<@NotNull String, @NotNull RuntimeException> exceptionFactory
    ) {
        if (value == 0 || value == 1) {
            return value == 1;
        } else {
            throw exceptionFactory.apply("Invalid value: " + value + " when should be only 0 or 1");
        }
    }

    /**
     * Convert a number to boolean which should be only 0 or 1 or throw an exception.
     *
     * @param value            the validated value.
     * @param exceptionFactory the exception factory.
     * @return the validated value.
     */
    public static boolean toBoolean(
        long value,
        @NotNull Function<@NotNull String, @NotNull RuntimeException> exceptionFactory
    ) {
        if (value == 0 || value == 1) {
            return value == 1;
        } else {
            throw exceptionFactory.apply("Invalid value: " + value + " when should be only 0 or 1");
        }
    }

    /**
     * Returns {@code true} if the numbers are equal to each other
     * and {@code false} otherwise.
     *
     * @param first  the first number.
     * @param second the second number to be compared with {@code first} for equality.
     * @return {@code true} if the arguments are equal to each other and {@code false} otherwise.
     * @since 9.7.0
     */
    public static boolean equals(byte first, byte second) {
        return first == second;
    }

    /**
     * Returns {@code true} if the numbers are equal to each other
     * and {@code false} otherwise.
     *
     * @param first  the first number.
     * @param second the second number to be compared with {@code first} for equality.
     * @return {@code true} if the arguments are equal to each other and {@code false} otherwise.
     * @since 9.7.0
     */
    public static boolean equals(short first, short second) {
        return first == second;
    }

    /**
     * Returns {@code true} if the numbers are equal to each other
     * and {@code false} otherwise.
     *
     * @param first  the first number.
     * @param second the second number to be compared with {@code first} for equality.
     * @return {@code true} if the arguments are equal to each other and {@code false} otherwise.
     * @since 9.7.0
     */
    public static boolean equals(int first, int second) {
        return first == second;
    }

    /**
     * Returns {@code true} if the numbers are equal to each other
     * and {@code false} otherwise.
     *
     * @param first  the first number.
     * @param second the second number to be compared with {@code first} for equality.
     * @return {@code true} if the arguments are equal to each other and {@code false} otherwise.
     * @since 9.7.0
     */
    public static boolean equals(long first, long second) {
        return first == second;
    }

    /**
     * Returns {@code true} if the numbers are equal to each other
     * and {@code false} otherwise.
     *
     * @param first  the first number.
     * @param second the second number to be compared with {@code first} for equality.
     * @return {@code true} if the arguments are equal to each other and {@code false} otherwise.
     * @since 9.7.0
     */
    public static boolean equals(float first, float second) {
        return first == second;
    }

    /**
     * Returns {@code true} if the numbers are equal to each other
     * and {@code false} otherwise.
     *
     * @param first  the first number.
     * @param second the second number to be compared with {@code first} for equality.
     * @return {@code true} if the arguments are equal to each other and {@code false} otherwise.
     * @since 9.7.0
     */
    public static boolean equals(double first, double second) {
        return first == second;
    }

    private NumberUtils() {
        throw new IllegalArgumentException();
    }
}
