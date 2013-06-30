package rlib.util.array;

import java.util.Comparator;

/**
 * Базовая модель компаратора для динамических массивов.
 *
 * @author Ronn
 */
public abstract class ArrayComparator<T> implements Comparator<T>
{
	@Override
	public int compare(T first, T second)
	{
		if(first == null)
			return 1;

		if(second == null)
			return -1;

		return compareImpl(first, second);
	}

	/**
	 * Сравнение 2х объектов.
	 */
	protected abstract int compareImpl(T first, T second);
}
