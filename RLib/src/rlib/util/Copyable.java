package rlib.util;

/**
 * Интерфейс для реализации метода копирования объекта.
 * 
 * @author Ronn
 */
public interface Copyable<T> {

	/**
	 * @return копия текущего объекта.
	 */
	public T copy();
}
