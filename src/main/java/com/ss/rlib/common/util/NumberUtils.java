package com.ss.rlib.common.util;

import org.jetbrains.annotations.NotNull;

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
    @Deprecated
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
    @Deprecated
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
    @Deprecated
    public static int makeInt(byte byte1, byte byte2, byte byte3, byte byte4) {
        return (byte4 & 0xFF) << 24 | (byte3 & 0xFF) << 16 | (byte2 & 0xFF) << 8 | byte1 & 0xFF;
    }

    /**
     * Make long long.
     *
     * @param bytes the bytes
     * @return the long
     */
    @Deprecated
    public static long makeLong(@NotNull byte[] bytes) {
        return ((long) bytes[7] & 0xFF) << 56 | ((long) bytes[6] & 0xFF) << 48 | ((long) bytes[5] & 0xFF) << 40 |
                ((long) bytes[4] & 0xFF) << 32 | ((long) bytes[3] & 0xFF) << 24 | ((long) bytes[2] & 0xFF) << 16 |
                ((long) bytes[1] & 0xFF) << 8 | (long) bytes[0] & 0xFF;
    }

    private NumberUtils() {
        throw new IllegalArgumentException();
    }
}
