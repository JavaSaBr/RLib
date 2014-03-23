package rlib.util.array;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.LockFactory;
import rlib.util.ArrayUtils;

/**
 * Реализация динамического массива с возможностью использовать потокобезопасную
 * запись и чтение.
 *
 * @author Ronn
 */
public class ConcurrentArray<E> extends AbstractArray<E> {

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
			ConcurrentArray.this.fastRemove(--ordinal);
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
			ConcurrentArray.this.fastRemove(--ordinal);
		}

		@Override
		public void slowRemove() {
			ConcurrentArray.this.slowRemove(--ordinal);
		}
	}

	private static final long serialVersionUID = 1L;

	/** блокировщик на чтение */
	private final Lock readLock;
	/** блокировщик на запись */
	private final Lock writeLock;
	/** кол-во элементов в колекции */
	private final AtomicInteger size;

	/** массив элементов */
	private volatile E[] array;

	/**
	 * @param type тип элементов в массиве.
	 */
	public ConcurrentArray(Class<E> type) {
		this(type, 10);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	public ConcurrentArray(Class<E> type, int size) {
		super(type, size);

		ReadWriteLock readWriteLock = LockFactory.newRWLock();

		this.size = new AtomicInteger();
		this.readLock = readWriteLock.readLock();
		this.writeLock = readWriteLock.writeLock();
	}

	@Override
	public ConcurrentArray<E> add(E element) {

		if(size() == array.length) {
			array = ArrayUtils.copyOf(array, array.length * 3 / 2 + 1);
		}

		array[size.getAndIncrement()] = element;

		return this;
	}

	@Override
	public final ConcurrentArray<E> addAll(Array<? extends E> elements) {

		if(elements == null || elements.isEmpty()) {
			return this;
		}

		int diff = size() + elements.size() - array.length;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, diff);
		}

		for(E element : elements.array()) {

			if(element == null) {
				break;
			}

			add(element);
		}

		return this;
	}

	@Override
	public final Array<E> addAll(E[] elements) {

		if(elements == null || elements.length < 1) {
			return this;
		}

		int diff = size() + elements.length - array.length;

		if(diff > 0) {
			array = ArrayUtils.copyOf(array, diff);
		}

		for(E element : elements) {
			add(element);
		}

		return this;
	}

	@Override
	public final E[] array() {
		return array;
	}

	@Override
	public final E fastRemove(int index) {

		if(index < 0) {
			return null;
		}

		E[] array = array();

		int length = size();

		if(length < 1 || index >= length) {
			return null;
		}

		size.decrementAndGet();
		length = size();

		E old = array[index];

		array[index] = array[length];
		array[length] = null;

		return old;
	}

	@Override
	public final E get(int index) {
		return array[index];
	}

	@Override
	public final ArrayIterator<E> iterator() {
		return new FastIterator();
	}

	@Override
	public final void readLock() {
		readLock.lock();
	}

	@Override
	public final void readUnlock() {
		readLock.unlock();
	}

	@Override
	public final void set(int index, E element) {

		if(index < 0 || index >= size() || element == null) {
			return;
		}

		E[] array = array();

		if(array[index] != null) {
			size.decrementAndGet();
		}

		array[index] = element;

		size.incrementAndGet();
	}

	@Override
	protected final void setArray(E[] array) {
		this.array = array;
	}

	@Override
	protected final void setSize(int size) {
		this.size.getAndSet(size);
	}

	@Override
	public final int size() {
		return size.get();
	}

	@Override
	public final E slowRemove(int index) {

		int length = size();

		if(index < 0 || length < 1) {
			return null;
		}

		E[] array = array();

		int numMoved = length - index - 1;

		E old = array[index];

		if(numMoved > 0) {
			System.arraycopy(array, index + 1, array, index, numMoved);
		}

		size.decrementAndGet();

		array[size.get()] = null;

		return old;
	}

	@Override
	public final ConcurrentArray<E> trimToSize() {

		int size = size();

		if(size == array.length) {
			return this;
		}

		array = ArrayUtils.copyOfRange(array, 0, size);
		return this;
	}

	@Override
	public final void writeLock() {
		writeLock.lock();
	}

	@Override
	public final void writeUnlock() {
		writeLock.unlock();
	}
}