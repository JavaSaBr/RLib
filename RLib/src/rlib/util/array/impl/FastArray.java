package rlib.util.array.impl;

import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayIterator;

/**
 * Быстрый динамический массив. Использовать только в неконкурентных местах.
 *
 * @author Ronn
 */
public class FastArray<E> extends AbstractArray<E> {

	/**
	 * Быстрый итератор массива.
	 *
	 * @author Ronn
	 */
	private final class FastIterator implements ArrayIterator<E> {

		/** текущая позиция в массиве */
		private int ordinal;

		public FastIterator() {
			super();

			ordinal = 0;
		}

		@Override
		public void fastRemove() {
			FastArray.this.fastRemove(--ordinal);
		}

		@Override
		public boolean hasNext() {
			return ordinal < size;
		}

		@Override
		public int index() {
			return ordinal - 1;
		}

		@Override
		public E next() {
			return array[ordinal++];
		}

		@Override
		public void remove() {
			FastArray.this.fastRemove(--ordinal);
		}

		@Override
		public void slowRemove() {
			FastArray.this.slowRemove(--ordinal);
		}
	}

	private static final long serialVersionUID = -8477384427415127978L;

	/** массив элементов */
	protected E[] array;

	/** кол-во элементов в колекции */
	protected int size;

	/**
	 * @param type тип элементов в массиве.
	 */
	public FastArray(final Class<E> type) {
		super(type);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	public FastArray(final Class<E> type, final int size) {
		super(type, size);
	}

	@Override
	public FastArray<E> add(final E element) {

		if(size == array.length) {
			array = ArrayUtils.copyOf(array, array.length >> 1);
		}

		array[size++] = element;

		return this;
	}

	@Override
	public final FastArray<E> addAll(final Array<? extends E> elements) {

		if(elements == null || elements.isEmpty()) {
			return this;
		}

		final int current = array.length;
		final int diff = size() + elements.size() - current;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
		}

		for(final E element : elements.array()) {

			if(element == null) {
				break;
			}

			add(element);
		}

		return this;
	}

	@Override
	public final Array<E> addAll(final E[] elements) {

		if(elements == null || elements.length < 1) {
			return this;
		}

		final int current = array.length;
		final int diff = size() + elements.length - current;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
		}

		for(final E element : elements) {
			add(element);
		}

		return this;
	}

	@Override
	public final E[] array() {
		return array;
	}

	@Override
	public final E fastRemove(final int index) {

		final E[] array = array();

		if(index < 0 || size < 1 || index >= size) {
			return null;
		}

		size -= 1;

		final E old = array[index];

		array[index] = array[size];
		array[size] = null;

		return old;
	}

	@Override
	public final E get(final int index) {
		return array[index];
	}

	@Override
	public final ArrayIterator<E> iterator() {
		return new FastIterator();
	}

	@Override
	public final void set(final int index, final E element) {

		if(index < 0 || index >= size || element == null) {
			return;
		}

		final E[] array = array();

		if(array[index] != null) {
			size -= 1;
		}

		array[index] = element;

		size += 1;
	}

	@Override
	protected final void setArray(final E[] array) {
		this.array = array;
	}

	@Override
	protected final void setSize(final int size) {
		this.size = size;
	}

	@Override
	public final int size() {
		return size;
	}

	@Override
	public final E slowRemove(final int index) {

		if(index < 0 || size < 1) {
			return null;
		}

		final E[] array = array();

		final int numMoved = size - index - 1;

		final E old = array[index];

		if(numMoved > 0) {
			System.arraycopy(array, index + 1, array, index, numMoved);
		}

		size -= 1;

		array[size] = null;

		return old;
	}

	@Override
	public final FastArray<E> trimToSize() {

		if(size == array.length) {
			return this;
		}

		array = ArrayUtils.copyOfRange(array, 0, size);

		return this;
	}
}