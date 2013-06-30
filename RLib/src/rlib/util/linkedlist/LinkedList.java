package rlib.util.linkedlist;

import java.io.Serializable;
import java.util.Deque;

import rlib.util.array.FuncElement;
import rlib.util.pools.Foldable;

/**
 * @author Ronn
 */
public interface LinkedList<E> extends Deque<E>, Cloneable, Serializable, Foldable
{
	/**
	 * Получание с удалением первого элемента.
	 * 
	 * @return первый элемент в очереди.
	 */
    public E take();
    
    /**
	 * Блокировка чтений для изменения массива.
	 */
	public void writeLock();

	/**
	 * Разблокировка чтения массива.
	 */
	public void writeUnlock();

	/**
	 * Применить функцию на все элементы в массиве.
	 *
	 * @param func применяемая функция.
	 */
	public void apply(FuncElement<? super E> func);
	
	/**
	 * Блокировка изменения массива на время чтения его.
	 */
	public void readLock();

	/**
	 * Разблокировка изменения массива.
	 */
	public void readUnlock();
	
	/**
	 * Получение элемента по номеру в списке.
	 * 
	 * @param index номер в списке.
	 * @return элемент.
	 */
	public E get(int index);
	
	/**
	 * @param object интересуемый объект.
	 * @return номер его в списке.
	 */
	public int indexOf(Object object);
}
