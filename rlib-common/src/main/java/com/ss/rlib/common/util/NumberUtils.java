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
     * Bytes to int int.
     *
     * @param array     the array
     * @param offset    the offset
     * @param bigEndian the big endian
     * @return the int
     */
    @Deprecated(forRemoval = true)
    public static int bytesToInt(@NotNull byte[] array, int offset, boolean bigEndian) {

        if (bigEndian) {
            return makeInt(array[offset], array[offset + 1], array[offset + 2], array[offset + 3]);
        }

        return makeInt(array[offset + 3], array[offset + 2], array[offset + 1], array[offset]);
    }

    /**
     * Bytes to u int long.
     *
     * @param array     the array
     * @param offset    the offset
     * @param bigEndian the big endian
     * @return the long
     */
    @Deprecated(forRemoval = true)
    public static long bytesToUInt(@NotNull byte[] array, int offset, boolean bigEndian) {

        long value = 0;

        if (bigEndian) {
            value = makeInt(array[offset], array[offset + 1], array[offset + 2], array[offset + 3]);
        } else {
            value = makeInt(array[offset + 3], array[offset + 2], array[offset + 1], array[offset]);
        }

        return value & 0xFFFFFFFFL;
    }

    /**
     * Make int int.
     *
     * @param byte1 the byte 1
     * @param byte2 the byte 2
     * @param byte3 the byte 3
     * @param byte4 the byte 4
     * @return the int
     */
    @Deprecated(forRemoval = true)
    public static int makeInt(byte byte1, byte byte2, byte byte3, byte byte4) {
        return (byte4 & 0xFF) << 24 | (byte3 & 0xFF) << 16 | (byte2 & 0xFF) << 8 | byte1 & 0xFF;
    }

    /**
     * Make long long.
     *
     * @param bytes the bytes
     * @return the long
     */
    @Deprecated(forRemoval = true)
    public static long makeLong(@NotNull byte[] bytes) {
        return ((long) bytes[7] & 0xFF) << 56 | ((long) bytes[6] & 0xFF) << 48 | ((long) bytes[5] & 0xFF) << 40 |
                ((long) bytes[4] & 0xFF) << 32 | ((long) bytes[3] & 0xFF) << 24 | ((long) bytes[2] & 0xFF) << 16 |
                ((long) bytes[1] & 0xFF) << 8 | (long) bytes[0] & 0xFF;
    }

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
     * Covert a byte to unsigned byte.
     *
     * @param value the byte.
     * @return the unsigned byte.
     * @see Byte#toUnsignedInt(byte)
     */
    @Deprecated(forRemoval = true)
    public static int toUnsignedByte(byte value) {
        return Byte.toUnsignedInt(value);
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

    private NumberUtils() {
        throw new IllegalArgumentException();
    }
}
