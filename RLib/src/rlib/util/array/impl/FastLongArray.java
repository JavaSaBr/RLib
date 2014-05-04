package rlib.util.array.impl;

import rlib.util.ArrayUtils;
import rlib.util.array.ArrayIterator;
import rlib.util.array.LongArray;

/**
 * Реализация не потокобезопасного динамического массива примитивов long.
 *
 * @author Ronn
 */
public class FastLongArray implements LongArray {

	/**
	 * Быстрый итератор массива.
	 *
	 * @author Ronn
	 */
	private final class FastIterator implements ArrayIterator<Long> {

		/** текущая позиция в массиве */
		private int ordinal;

		public FastIterator() {
			ordinal = 0;
		}

		@Override
		public void fastRemove() {
			FastLongArray.this.fastRemove(--ordinal);
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
		public Long next() {
			return array[ordinal++];
		}

		@Override
		public void remove() {
			FastLongArray.this.fastRemove(--ordinal);
		}

		@Override
		public void slowRemove() {
			FastLongArray.this.slowRemove(--ordinal);
		}
	}

	/** массив элементов */
	protected long[] array;

	/** кол-во элементов в колекции */
	protected int size;

	/**
	 * @param type тип элементов в массиве.
	 */
	public FastLongArray() {
		this(10);
	}

	public FastLongArray(final int size) {
		this.array = new long[size];
		this.size = 0;
	}

	@Override
	public FastLongArray add(final long element) {

		if(size == array.length) {
			array = ArrayUtils.copyOf(array, array.length >> 1);
		}

		array[size++] = element;

		return this;
	}

	@Override
	public final FastLongArray addAll(final long[] elements) {

		if(elements == null || elements.length < 1) {
			return this;
		}

		final int current = array.length;
		final int diff = size() + elements.length - current;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
		}

		for(int i = 0, length = elements.length; i < length; i++) {
			add(elements[i]);
		}

		return this;
	}

	@Override
	public final FastLongArray addAll(final LongArray elements) {

		if(elements == null || elements.isEmpty()) {
			return this;
		}

		final int current = array.length;
		final int diff = size() + elements.size() - current;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
		}

		long[] array = elements.array();

		for(int i = 0, length = elements.size(); i < length; i++) {
			add(array[i]);
		}

		return this;
	}

	@Override
	public final long[] array() {
		return array;
	}

	@Override
	public final FastLongArray clear() {
		size = 0;
		return this;
	}

	@Override
	public final boolean fastRemove(final int index) {

		if(index < 0 || size < 1 || index >= size) {
			return false;
		}

		final long[] array = array();

		size -= 1;

		array[index] = array[size];
		array[size] = 0;

		return true;
	}

	@Override
	public final long first() {

		if(size < 1) {
			return 0;
		}

		return array[0];
	}

	@Override
	public final long get(final int index) {
		return array[index];
	}

	@Override
	public final int indexOf(final long element) {

		final long[] array = array();

		for(int i = 0, length = size; i < length; i++) {

			final long val = array[i];

			if(element == val) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public final boolean isEmpty() {
		return size < 1;
	}

	@Override
	public final ArrayIterator<Long> iterator() {
		return new FastIterator();
	}

	@Override
	public final long last() {

		if(size < 1) {
			return 0;
		}

		return array[size - 1];
	}

	@Override
	public final long poll() {
		final long val = first();
		slowRemove(0);
		return val;
	}

	@Override
	public final long pop() {
		final long last = last();
		fastRemove(size - 1);
		return last;
	}

	@Override
	public final int size() {
		return size;
	}

	@Override
	public final boolean slowRemove(final int index) {

		if(index < 0 || size < 1) {
			return false;
		}

		final long[] array = array();

		final int numMoved = size - index - 1;

		if(numMoved > 0) {
			System.arraycopy(array, index + 1, array, index, numMoved);
		}

		size -= 1;
		array[size] = 0;

		return true;
	}

	@Override
	public final FastLongArray sort() {
		ArrayUtils.sort(array, 0, size);
		return this;
	}

	@Override
	public String toString() {
		return ArrayUtils.toString(this);
	}

	@Override
	public final FastLongArray trimToSize() {

		long[] array = array();

		if(size == array.length) {
			return this;
		}

		array = ArrayUtils.copyOfRange(array, 0, size);

		return this;
	}

}