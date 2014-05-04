package rlib.util.array.impl;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsynReadSynWriteLock;
import rlib.concurrent.lock.LockFactory;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayIterator;

/**
 * Реализация динамического массива с возможностью потокобезопасно асинхронно
 * читать и синхронно записывать. Не поддерживается рекурсивный вызов
 * readLock/writeLock, использовать рекомендуется в местах, где запись намного
 * реже чтения и где важно по минимуму нагружать GC.
 * 
 * Все операции по чтению массива производить в блоке
 * 
 * <pre>
 *
 * array.readLock()/writeLock();
 * try {
 * 	// handle
 * } finally {
 * 	array.readUnlock()/writeUnlock();
 * }
 * </pre>
 *
 * @author Ronn
 */
public class ConcurrentAtomicArray<E> extends AbstractArray<E> {

	private static final long serialVersionUID = -6291504312637658721L;

	/** кол-во элементов в колекции */
	private final AtomicInteger size;
	/** блокировщик */
	private final AsynReadSynWriteLock lock;

	/** массив элементов */
	private volatile E[] array;

	/**
	 * @param type тип элементов в массиве.
	 */
	public ConcurrentAtomicArray(final Class<E> type) {
		this(type, 10);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	public ConcurrentAtomicArray(final Class<E> type, final int size) {
		super(type, size);

		this.size = new AtomicInteger();
		this.lock = LockFactory.newPrimitiveAtomicARSWLock();
	}

	@Override
	public ConcurrentAtomicArray<E> add(final E element) {

		if(size() == array.length) {
			array = ArrayUtils.copyOf(array, array.length >> 1);
		}

		array[size.getAndIncrement()] = element;

		return this;
	}

	@Override
	public final ConcurrentAtomicArray<E> addAll(final Array<? extends E> elements) {

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

		if(index < 0) {
			return null;
		}

		final E[] array = array();

		int length = size();

		if(length < 1 || index >= length) {
			return null;
		}

		length = size.decrementAndGet();

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
		return new ArrayIteratorImpl<E>(this);
	}

	@Override
	public final void readLock() {
		lock.asynLock();
	}

	@Override
	public final void readUnlock() {
		lock.asynUnlock();
	}

	@Override
	public final void set(final int index, final E element) {

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
	public final E slowRemove(final int index) {

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

		array[size.decrementAndGet()] = null;

		return old;
	}

	@Override
	public final ConcurrentAtomicArray<E> trimToSize() {

		final int size = size();

		if(size == array.length) {
			return this;
		}

		array = ArrayUtils.copyOfRange(array, 0, size);
		return this;
	}

	@Override
	public final void writeLock() {
		lock.synLock();
	}

	@Override
	public final void writeUnlock() {
		lock.synUnlock();
	}
}