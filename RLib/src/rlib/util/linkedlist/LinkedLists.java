package rlib.util.linkedlist;

/**
 * Утильный класс по работе со связанными списками.
 * 
 * @author Ronn
 */
public final class LinkedLists
{
	private LinkedLists()
	{
		throw new RuntimeException();
	}
	
	/**
	 * Создание быстрого связанного списка.
	 * 
	 * @param type тип элементов в списке.
	 * @return новый связанный список.
	 */
	public static <E> LinkedList<E> newLinkedList(Class<?> type)
	{
		return new FastLinkedList<E>(type);
	}
}
