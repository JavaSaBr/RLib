package rlib.util.array;

import rlib.util.ArrayUtils;

/**
 * Быстрый динамический массив примитивных int.
 *
 * @author Ronn
 */
public class FastIntegerArray implements IntegerArray {

	/**
	 * Быстрый итератор массива.
	 *
	 * @author Ronn
	 */
	private final class FastIterator implements ArrayIterator<Integer> {

		/** текущая позиция в массиве */
		private int ordinal;

		public FastIterator() {
			ordinal = 0;
		}

		@Override
		public void fastRemove() {
			FastIntegerArray.this.fastRemove(--ordinal);
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
		public Integer next() {
			return array[ordinal++];
		}

		@Override
		public void remove() {
			FastIntegerArray.this.fastRemove(--ordinal);
		}

		@Override
		public void slowRemove() {
			FastIntegerArray.this.slowRemove(--ordinal);
		}
	}

	/** массив элементов */
	protected int[] array;

	/** кол-во элементов в колекции */
	protected int size;

	/**
	 * @param type тип элементов в массиве.
	 */
	public FastIntegerArray() {
		this(10);
	}

	public FastIntegerArray(final int size) {
		this.array = new int[size];
		this.size = 0;
	}

	@Override
	public FastIntegerArray add(final int element) {

		if(size == array.length) {
			array = ArrayUtils.copyOf(array, array.length * 3 / 2 + 1);
		}

		array[size++] = element;

		return this;
	}

	@Override
	public final FastIntegerArray addAll(final int[] elements) {

		if(elements == null || elements.length < 1) {
			return this;
		}

		final int diff = size + elements.length - array.length;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, diff);
		}

		for(int i = 0, length = elements.length; i < length; i++) {
			add(elements[i]);
		}

		return this;
	}

	@Override
	public final FastIntegerArray addAll(final IntegerArray elements) {

		if(elements == null || elements.isEmpty()) {
			return this;
		}

		final int diff = size + elements.size() - array.length;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, diff);
		}

		array = elements.array();

		for(int i = 0, length = elements.size(); i < length; i++) {
			add(array[i]);
		}

		return this;
	}

	@Override
	public final int[] array() {
		return array;
	}

	@Override
	public final FastIntegerArray clear() {
		size = 0;
		return this;
	}

	@Override
	public final boolean contains(final int element) {

		final int[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			if(array[i] == element) {
				return true;
			}
		}

		return false;
	}

	@Override
	public final boolean containsAll(final int[] array) {

		for(int i = 0, length = array.length; i < length; i++) {
			if(!contains(array[i])) {
				return false;
			}
		}

		return true;
	}

	@Override
	public final boolean containsAll(final IntegerArray array) {

		final int[] elements = array.array();

		for(int i = 0, length = array.size(); i < length; i++) {
			if(!contains(elements[i])) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean fastRemove(final int element) {

		final int index = indexOf(element);

		if(index > -1) {
			fastRemoveByIndex(index);
		}

		return index > -1;
	}

	@Override
	public final boolean fastRemoveByIndex(final int index) {

		if(index < 0 || size < 1 || index >= size) {
			return false;
		}

		final int[] array = array();

		size -= 1;

		array[index] = array[size];
		array[size] = 0;

		return true;
	}

	@Override
	public final int first() {

		if(size < 1) {
			return 0;
		}

		return array[0];
	}

	@Override
	public final int get(final int index) {
		return array[index];
	}

	@Override
	public final int indexOf(final int element) {

		final int[] array = array();

		for(int i = 0, length = size; i < length; i++) {

			final int val = array[i];

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
	public final ArrayIterator<Integer> iterator() {
		return new FastIterator();
	}

	@Override
	public final int last() {

		if(size < 1) {
			return 0;
		}

		return array[size - 1];
	}

	@Override
	public final int lastIndexOf(final int element) {

		final int[] array = array();

		int last = -1;

		for(int i = 0, length = size; i < length; i++) {

			final int val = array[i];

			if(element == val) {
				last = i;
			}
		}

		return last;
	}

	@Override
	public final int poll() {
		final int val = first();
		slowRemoveByIndex(0);
		return val;
	}

	@Override
	public final int pop() {
		final int last = last();
		fastRemoveByIndex(size - 1);
		return last;
	}

	@Override
	public final boolean removeAll(final IntegerArray target) {

		if(target.isEmpty()) {
			return true;
		}

		final int[] array = target.array();

		for(int i = 0, length = target.size(); i < length; i++) {
			fastRemove(array[i]);
		}

		return true;
	}

	@Override
	public final boolean retainAll(final IntegerArray target) {

		final int[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			if(!target.contains(array[i])) {
				fastRemoveByIndex(i--);
				length--;
			}
		}

		return true;
	}

	@Override
	public final int size() {
		return size;
	}

	@Override
	public boolean slowRemove(final int element) {

		final int index = indexOf(element);

		if(index > -1) {
			slowRemoveByIndex(index);
		}

		return index > -1;
	}

	@Override
	public final boolean slowRemoveByIndex(final int index) {

		if(index < 0 || size < 1) {
			return false;
		}

		final int[] array = array();

		final int numMoved = size - index - 1;

		if(numMoved > 0) {
			System.arraycopy(array, index + 1, array, index, numMoved);
		}

		size -= 1;

		array[size] = 0;

		return true;
	}

	@Override
	public final FastIntegerArray sort() {
		ArrayUtils.sort(array, 0, size);
		return this;
	}

	@Override
	public final int[] toArray(final int[] container) {

		final int[] array = array();

		if(container.length >= size) {

			for(int i = 0, j = 0, length = array.length, newLength = container.length; i < length && j < newLength; i++) {
				container[j++] = array[i];
			}

			return container;
		}

		return array;
	}

	@Override
	public String toString() {
		return ArrayUtils.toString(this);
	}

	@Override
	public final FastIntegerArray trimToSize() {

		int[] array = array();

		if(size == array.length) {
			return this;
		}

		array = ArrayUtils.copyOfRange(array, 0, size);

		return this;
	}
}