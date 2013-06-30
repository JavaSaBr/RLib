package rlib.util.array;

import java.util.Iterator;

/**
 * Интерфейс для реализации итератора массива.
 * 
 * @author Ronn
 */
public interface ArrayIterator<E> extends Iterator<E>
{
	/**
	 * Удалить с переносом последнего элемента на место удаленного.
	 */
	public void fastRemove();
	
	/**
	 * @return позиция элемента в массиве.
	 */
	public int index();
	
	/**
	 * Удалить со сдвигом следующих элементов.
	 */
	public void slowRemove();
}
