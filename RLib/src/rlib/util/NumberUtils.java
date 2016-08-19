package rlib.util;

/**
 * Набор утильных методов по работе с числами.
 *
 * @author JavaSaBr
 */
public final class NumberUtils {

    public static int bytesToInt(final byte[] array, final int offset, final boolean bigEndian) {

        if (bigEndian) {
            return makeInt(array[offset], array[offset + 1], array[offset + 2], array[offset + 3]);
        }

        return makeInt(array[offset + 3], array[offset + 2], array[offset + 1], array[offset]);
    }

    public static long bytesToUInt(final byte[] array, final int offset, final boolean bigEndian) {

        long value = 0;

        if (bigEndian) {
            value = makeInt(array[offset], array[offset + 1], array[offset + 2], array[offset + 3]);
        } else {
            value = makeInt(array[offset + 3], array[offset + 2], array[offset + 1], array[offset]);
        }

        return value & 0xFFFFFFFFL;
    }

    /**
     * Конвектирование байтов в int.
     */
    public static int makeInt(final byte byte1, final byte byte2, final byte byte3, final byte byte4) {
        return (byte4 & 0xFF) << 24 | (byte3 & 0xFF) << 16 | (byte2 & 0xFF) << 8 | byte1 & 0xFF;
    }

    /**
     * Конвектирование байтов в int.
     */
    public static long makeLong(final byte[] bytes) {
        return ((long) bytes[7] & 0xFF) << 56 | ((long) bytes[6] & 0xFF) << 48 | ((long) bytes[5] & 0xFF) << 40 | ((long) bytes[4] & 0xFF) << 32 | ((long) bytes[3] & 0xFF) << 24 | ((long) bytes[2] & 0xFF) << 16 | ((long) bytes[1] & 0xFF) << 8 | (long) bytes[0] & 0xFF;
    }

    public static boolean equals(float first, float second) {
        return Float.floatToIntBits(first) == Float.floatToIntBits(second);
    }

    public static boolean equals(double first, double second) {
        return Double.doubleToLongBits(first) == Double.doubleToLongBits(second);
    }

    private NumberUtils() {
        throw new IllegalArgumentException();
    }
}
