package rlib.util.array.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayIterator;

/**
 * Реализация итератора динамического массива.
 * 
 * @author Ronn
 */
public class ArrayIteratorImpl<E> implements ArrayIterator<E> {

	/** итерируемая коллекция */
	private final Array<E> collection;
	/** итерируемый массив */
	private final E[] array;

	/** текущая позиция в массиве */
	private int ordinal;

	public ArrayIteratorImpl(final Array<E> collection) {
		this.collection = collection;
		this.array = collection.array();
	}

	@Override
	public void fastRemove() {
		collection.fastRemove(--ordinal);
	}

	@Override
	public boolean hasNext() {
		return ordinal < collection.size();
	}

	@Override
	public int index() {
		return ordinal - 1;
	}

	@Override
	public E next() {

		if(ordinal >= array.length) {
			return null;
		}

		return array[ordinal++];
	}

	@Override
	public void remove() {
		collection.fastRemove(--ordinal);
	}

	@Override
	public void slowRemove() {
		collection.fastRemove(--ordinal);
	}
}
