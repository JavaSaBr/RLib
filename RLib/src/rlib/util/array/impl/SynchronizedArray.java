package rlib.util.array.impl;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayIterator;

/**
 * Реализация динамического массива с возможностью использовать потокобезопасную
 * запись и чтение.
 *
 * @author Ronn
 */
public class SynchronizedArray<E> extends AbstractArray<E> {

	/**
	 * Быстрый итератор по массиву.
	 *
	 * @author Ronn
	 */
	private final class FastIterator implements ArrayIterator<E> {

		/** текущая позиция в массиве */
		private int ordinal;

		@Override
		public void fastRemove() {
			SynchronizedArray.this.fastRemove(--ordinal);
		}

		@Override
		public boolean hasNext() {
			return ordinal < size();
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
			SynchronizedArray.this.fastRemove(--ordinal);
		}

		@Override
		public void slowRemove() {
			SynchronizedArray.this.slowRemove(--ordinal);
		}
	}

	private static final long serialVersionUID = 1L;

	/** кол-во элементов в колекции */
	private final AtomicInteger size;

	/** массив элементов */
	private volatile E[] array;

	/**
	 * @param type тип элементов в массиве.
	 */
	public SynchronizedArray(final Class<E> type) {
		this(type, 10);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	public SynchronizedArray(final Class<E> type, final int size) {
		super(type, size);

		this.size = new AtomicInteger();
	}

	@Override
	public synchronized SynchronizedArray<E> add(final E element) {

		if(size() == array.length) {
			array = ArrayUtils.copyOf(array, array.length >> 1);
		}

		array[size.getAndIncrement()] = element;

		return this;
	}

	@Override
	public synchronized final SynchronizedArray<E> addAll(final Array<? extends E> elements) {

		if(elements == null || elements.isEmpty()) {
			return this;
		}

		final int diff = size() + elements.size() - array.length;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, diff);
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
	public synchronized final Array<E> addAll(final E[] elements) {

		if(elements == null || elements.length < 1) {
			return this;
		}

		final int diff = size() + elements.length - array.length;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, diff);
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
	public synchronized final E fastRemove(final int index) {

		if(index < 0) {
			return null;
		}

		final E[] array = array();

		int length = size();

		if(length < 1 || index >= length) {
			return null;
		}

		size.decrementAndGet();
		length = size();

		final E old = array[index];

		array[index] = array[length];
		array[length] = null;

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
	public synchronized final void set(final int index, final E element) {

		if(index < 0 || index >= size() || element == null) {
			return;
		}

		final E[] array = array();

		if(array[index] != null) {
			size.decrementAndGet();
		}

		array[index] = element;

		size.incrementAndGet();
	}

	@Override
	protected final void setArray(final E[] array) {
		this.array = array;
	}

	@Override
	protected final void setSize(final int size) {
		this.size.getAndSet(size);
	}

	@Override
	public final int size() {
		return size.get();
	}

	@Override
	public synchronized final E slowRemove(final int index) {

		final int length = size();

		if(index < 0 || length < 1) {
			return null;
		}

		final E[] array = array();

		final int numMoved = length - index - 1;

		final E old = array[index];

		if(numMoved > 0) {
			System.arraycopy(array, index + 1, array, index, numMoved);
		}

		size.decrementAndGet();

		array[size.get()] = null;

		return old;
	}

	@Override
	public final SynchronizedArray<E> trimToSize() {

		final int size = size();

		if(size == array.length) {
			return this;
		}

		array = ArrayUtils.copyOfRange(array, 0, size);
		return this;
	}
}