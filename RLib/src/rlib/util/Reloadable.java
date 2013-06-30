package rlib.util;

/**
 * Класс, реализовывающий этот интерфейс, должен уметь обновлятся на новый вариано самого себя.
 *
 * @author Ronn
 * @created 28.03.2012
 */
public interface Reloadable<E>
{
	/**
	 * Обновится до состояния указанного объекта.
	 * 
	 * @param update новый вариант объекта.
	 */
	public void reload(E update);
}
