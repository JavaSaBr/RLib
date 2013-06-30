package rlib.util.wraps;

import rlib.util.pools.Foldable;

/**
 * Интерфейс для реализации обертки.
 *
 * @author Ronn
 */
public interface Wrap extends Foldable
{
	/**
	 * Сложить в пул.
	 */
	public void fold();

	/**
	 * @return обернутый байт.
	 */
	public byte getByte();

	/**
	 * @return обернутый чар.
	 */
	public char getChar();

	/**
	 * @return обернутый дабл.
	 */
	public double getDouble();

	/**
	 * @return обернутый флоат.
	 */
	public float getFloat();

	/**
	 * @return обернутый инт.
	 */
	public int getInt();

	/**
	 * @return обернутый лонг.
	 */
	public long getLong();

	/**
	 * @return обернутый объект.
	 */
	public Object getObject();

	/**
	 * @return обернутый шорт.
	 */
	public short getShort();

	/**
	 * @return тип обертки.
	 */
	public WrapType getWrapType();

	/**
	 * @param value байт, который нужно обернуть.
	 */
	public void setByte(byte value);

	/**
	 * @param value чар, который нужно обернуть.
	 */
	public void setChar(char value);

	/**
	 * @param value дабл, который нужно обернуть.
	 */
	public void setDouble(double value);


	/**
	 * @param value флоат, который нужно обернуть.
	 */
	public void setFloat(float value);

	/**
	 * @param value инт, который нужно обернуть.
	 */
	public void setInt(int value);

	/**
	 * @param value лонг, который нужно обернуть.
	 */
	public void setLong(long value);

	/**
	 * @param object объект, который нужно обернуть.
	 */
	public void setObject(Object object);

	/**
	 * @param value шорт, который нужно обернуть.
	 */
	public void setShort(short value);
}
