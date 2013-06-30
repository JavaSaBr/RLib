package rlib.util;

/**
 * Набор утильных методов по работе с числами.
 *
 * @author Ronn
 */
public final class NumberUtils
{
	private NumberUtils()
	{
		throw new IllegalArgumentException();
	}

	public static long bytesToUInt(byte[] array, int offset, boolean bigEndian)
	{
		long value = 0;

		if(bigEndian)
			value = (long) makeInt(array[offset], array[offset + 1], array[offset + 2], array[offset + 3]);
		else
			value = (long) makeInt(array[offset + 3], array[offset + 2], array[offset + 1], array[offset]);

		return value & 0xFFFFFFFFL;
	}

	public static int bytesToInt(byte[] array, int offset, boolean bigEndian)
	{
		if(bigEndian)
			return makeInt(array[offset], array[offset + 1], array[offset + 2], array[offset + 3]);
		else
			return makeInt(array[offset + 3], array[offset + 2], array[offset + 1], array[offset]);
	}

	/**
	 * Конвектирование байтов в int.
	 */
	private static int makeInt(byte byte1, byte byte2, byte byte3, byte byte4)
	{
		return ((byte4 & 0xFF) << 24) | ((byte3 & 0xFF) << 16) | ((byte2 & 0xFF) << 8) | (byte1 & 0xFF);
	}
}
