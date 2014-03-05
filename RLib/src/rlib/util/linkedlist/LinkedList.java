package rlib.util.linkedlist;

import java.io.Serializable;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.util.pools.Foldable;

/**
 * Интерфей с для реализации связанного списка.
 * 
 * @author Ronn
 */
public interface LinkedList<E> extends Deque<E>, Cloneable, Serializable, Foldable {

	/**
	 * Применить функцию на все элементы в массиве.
	 *
	 * @param consumer применяемая функция.
	 */
	public void accept(Consumer<? super E> consumer);

	/**
	 * Применить функцию замены всех элементов.
	 *
	 * @param function применяемая функция.
	 */
	public void apply(Function<? super E, ? extends E> function);

	/**
	 * Получение элемента по номеру в списке.
	 * 
	 * @param index номер в списке.
	 * @return элемент.
	 */
	public E get(int index);

	/**
	 * @return первый узел списка.
	 */
	public Node<E> getFirstNode();

	/**
	 * @return последний узел списка.
	 */
	public Node<E> getLastNode();

	/**
	 * @param object интересуемый объект.
	 * @return номер его в списке.
	 */
	public int indexOf(Object object);

	/**
	 * Блокировка изменения массива на время чтения его.
	 */
	public void readLock();

	/**
	 * Разблокировка изменения массива.
	 */
	public void readUnlock();

	/**
	 * Получание с удалением первого элемента.
	 * 
	 * @return первый элемент в очереди.
	 */
	public E take();

	/**
	 * Удаление узла в списке.
	 * 
	 * @param node удаляемый узел.
	 * @return удаленный элемент из узла.
	 */
	public E unlink(Node<E> node);

	/**
	 * Блокировка чтений для изменения массива.
	 */
	public void writeLock();

	/**
	 * Разблокировка чтения массива.
	 */
	public void writeUnlock();
}
