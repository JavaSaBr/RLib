package rlib.util.array;

/**
 * Интерфейс для реализации функции на элемент в массиве.
 *
 * @author Ronn
 */
public interface FuncElement<E>
{
	/**
	 * Применить функцию на указанный элемент.
	 *
	 * @param element элемент в массиве.
	 */
	public void apply(E element);
}
