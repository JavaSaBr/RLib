package rlib.util.array.impl;

import rlib.util.ArrayUtils;

/**
 * Реализация сортированного {@link FastArray}, где сортировка происходит при
 * вставке.
 * 
 * @see FastArray
 * @author Ronn
 * @created 28.02.2012
 */
public class SortedArray<E extends Comparable<E>> extends FastArray<E> {

	private static final long serialVersionUID = 1L;

	/**
	 * @param type тип элементов в массиве.
	 */
	public SortedArray(final Class<E> type) {
		super(type);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	public SortedArray(final Class<E> type, final int size) {
		super(type, size);
	}

	@Override
	public SortedArray<E> add(final E element) {

		if(size == array.length) {
			array = ArrayUtils.copyOf(array, array.length * 3 / 2 + 1);
		}

		final E[] array = array();

		for(int i = 0, length = array.length; i < length; i++) {

			final E old = array[i];

			if(old == null) {
				array[i] = element;
				size++;
				return this;
			}

			if(element.compareTo(old) < 0) {

				size++;

				final int numMoved = size - i - 1;

				System.arraycopy(array, i, array, i + 1, numMoved);

				array[i] = element;
				return this;
			}
		}

		return this;
	}
}