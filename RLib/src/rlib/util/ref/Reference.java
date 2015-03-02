package rlib.util.ref;

import rlib.util.pools.Foldable;

/**
 * Интерфейс для реализации ссылки на какие-нибудь данные.
 * 
 * @author Ronn
 */
public interface Reference extends Foldable {

	/**
	 * @return байт, на который ссылается ссылка.
	 */
	public default byte getByte() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return символ, на который ссылается ссылка.
	 */
	public default char getChar() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return дробное число, на которое ссылается ссылка.
	 */
	public default double getDouble() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return дробное число, на которое ссылается ссылка.
	 */
	public default float getFloat() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return целое число, на которое ссылается ссылка.
	 */
	public default int getInt() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return целое число, на которое ссылается ссылка.
	 */
	public default long getLong() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return объект, на который ссылается ссылка.
	 */
	public default Object getObject() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return тип ссылки.
	 */
	public default ReferenceType getReferenceType() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @return целое число, на которое ссылается ссылка.
	 */
	public default short getShort() {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value байт, на который нужно сослаться.
	 */
	public default void setByte(final byte value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value символ, на который нужно сослаться.
	 */
	public default void setChar(final char value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value дробное число, на которое нужно сослаться.
	 */
	public default void setDouble(final double value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value дробное число, на которое нужно сослаться.
	 */
	public default void setFloat(final float value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value целое число, на которое нужно сослаться.
	 */
	public default void setInt(final int value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value целое число, на которое нужно сослаться.
	 */
	public default void setLong(final long value) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param object объект, на который нужно сослаться.
	 */
	public default void setObject(final Object object) {
		throw new RuntimeException("not implemented.");
	}

	/**
	 * @param value целое число, на которое нужно сослаться.
	 */
	public default void setShort(final short value) {
		throw new RuntimeException("not implemented.");
	}
}
