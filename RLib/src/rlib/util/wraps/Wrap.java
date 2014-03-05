package rlib.util.wraps;

import rlib.util.pools.Foldable;

/**
 * Интерфейс для реализации обертки.
 * 
 * @author Ronn
 */
public interface Wrap extends Foldable {

	/**
	 * Сложить в пул.
	 */
	default public void fold() {
	}

	/**
	 * @return обернутый байт.
	 */
	default public byte getByte() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return обернутый чар.
	 */
	default public char getChar() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return обернутый дабл.
	 */
	default public double getDouble() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return обернутый флоат.
	 */
	default public float getFloat() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return обернутый инт.
	 */
	default public int getInt() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return обернутый лонг.
	 */
	default public long getLong() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return обернутый объект.
	 */
	default public Object getObject() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return обернутый шорт.
	 */
	default public short getShort() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return тип обертки.
	 */
	default public WrapType getWrapType() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value байт, который нужно обернуть.
	 */
	default public void setByte(byte value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value чар, который нужно обернуть.
	 */
	default public void setChar(char value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value дабл, который нужно обернуть.
	 */
	default public void setDouble(double value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value флоат, который нужно обернуть.
	 */
	default public void setFloat(float value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value инт, который нужно обернуть.
	 */
	default public void setInt(int value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value лонг, который нужно обернуть.
	 */
	default public void setLong(long value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param object объект, который нужно обернуть.
	 */
	default public void setObject(Object object) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value шорт, который нужно обернуть.
	 */
	default public void setShort(short value) {
		throw new RuntimeException("not implemented.");
	}
}
