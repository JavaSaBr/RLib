package rlib.util.array;

/**
 * Интерфейс для реализации поиска нужного элемента в массиве
 *
 * @author Ronn
 * @created 12.04.2012
 */
public interface Search<E> {

	/**
	 * Проверка, подходит ли элемент к указанному
	 * 
	 * @param required
	 * @param target
	 */
	public boolean compare(E required, E target);
}
