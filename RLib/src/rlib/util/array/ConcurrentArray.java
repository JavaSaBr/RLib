package rlib.util.array;

import java.util.Comparator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.concurrent.Locks;

/**
 * Динамический конкурентный массив объектов. Используется синхронная запись и
 * асинхронное чтение.
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
			return ordinal < size;
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

	/** массив элементов */
	private volatile E[] array;

	/** кол-во элементов в колекции */
	private volatile int size;

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

		ReadWriteLock readWriteLock = Locks.newRWLock();

		this.readLock = readWriteLock.readLock();
		this.writeLock = readWriteLock.writeLock();
	}

	@Override
	public void accept(Consumer<? super E> consumer) {
		readLock();
		try {

			E[] array = array();

			for(int i = 0, length = size; i < length; i++) {
				consumer.accept(array[i]);
			}

		} finally {
			readUnlock();
		}
	}

	@Override
	public ConcurrentArray<E> add(E element) {
		writeLock();
		try {

			if(size < 0) {
				size = 0;
			}

			if(size >= array.length) {
				array = Arrays.copyOf(array, array.length * 3 / 2 + 1);
			}

			array[size] = element;

			size += 1;

			return this;
		} finally {
			writeUnlock();
		}
	}

	@Override
	public final ConcurrentArray<E> addAll(Array<? extends E> elements) {

		if(elements == null || elements.isEmpty()) {
			return this;
		}

		writeLock();
		try {

			int diff = size + elements.size() - array.length;

			if(diff > 0) {
				array = Arrays.copyOf(array, diff);
			}

			E[] array = elements.array();

			for(int i = 0, length = elements.size(); i < length; i++) {
				add(array[i]);
			}

			return this;

		} finally {
			writeUnlock();
		}
	}

	@Override
	public final Array<E> addAll(E[] elements) {

		if(elements == null || elements.length < 1) {
			return this;
		}

		writeLock();
		try {

			int diff = size + elements.length - array.length;

			if(diff > 0) {
				array = Arrays.copyOf(array, diff);
			}

			for(int i = 0, length = elements.length; i < length; i++) {
				add(elements[i]);
			}

			return this;

		} finally {
			writeUnlock();
		}
	}

	@Override
	public void apply(Function<? super E, ? extends E> function) {
		readLock();
		try {

			E[] array = array();

			for(int i = 0, length = size; i < length; i++) {
				array[i] = function.apply(array[i]);
			}

		} finally {
			readUnlock();
		}
	}

	@Override
	public final E[] array() {
		return array;
	}

	@Override
	public final ConcurrentArray<E> clear() {
		writeLock();
		try {

			Arrays.clear(array);

			size = 0;

			return this;

		} finally {
			writeUnlock();
		}
	}

	@Override
	public final boolean contains(Object object) {
		readLock();
		try {

			E[] array = array();

			for(int i = 0, length = size; i < length; i++) {
				if(array[i].equals(object)) {
					return true;
				}
			}

			return false;

		} finally {
			readUnlock();
		}
	}

	@Override
	public final E fastRemove(int index) {

		if(index < 0) {
			return null;
		}

		writeLock();
		try {

			E[] array = array();

			if(size < 1 || index >= size) {
				return null;
			}

			size -= 1;

			E old = array[index];

			array[index] = array[size];
			array[size] = null;

			return old;

		} finally {
			writeUnlock();
		}
	}

	@Override
	public final E first() {
		readLock();
		try {

			if(size < 1) {
				return null;
			}

			return array[0];

		} finally {
			readUnlock();
		}
	}

	@Override
	public final E get(int index) {
		return array[index];
	}

	@Override
	public final int indexOf(Object object) {

		if(object == null) {
			return -1;
		}

		readLock();
		try {

			E[] array = array();

			for(int i = 0, length = size; i < length; i++) {

				E element = array[i];

				if(element.equals(object)) {
					return i;
				}
			}

			return -1;

		} finally {
			readUnlock();
		}
	}

	@Override
	public final boolean isEmpty() {
		return size < 1;
	}

	@Override
	public final ArrayIterator<E> iterator() {
		return new FastIterator();
	}

	@Override
	public final E last() {
		readLock();
		try {

			if(size < 1) {
				return null;
			}

			return array[size - 1];

		} finally {
			readUnlock();
		}
	}

	@Override
	public final int lastIndexOf(Object object) {

		if(object == null) {
			return -1;
		}

		readLock();
		try {

			E[] array = array();

			int last = -1;

			for(int i = 0, length = size; i < length; i++) {

				E element = array[i];

				if(element.equals(object)) {
					last = i;
				}
			}

			return last;

		} finally {
			readUnlock();
		}
	}

	@Override
	public final E poll() {
		return slowRemove(0);
	}

	@Override
	public final E pop() {
		return fastRemove(size - 1);
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
	public final boolean removeAll(Array<?> target) {

		if(target.isEmpty()) {
			return true;
		}

		writeLock();
		try {

			Object[] array = target.array();

			for(int i = 0, length = target.size(); i < length; i++) {
				fastRemove(array[i]);
			}

		} finally {
			writeUnlock();
		}

		return true;
	}

	@Override
	public final boolean retainAll(Array<?> target) {
		writeLock();
		try {

			E[] array = array();

			for(int i = 0, length = size; i < length; i++)

				if(!target.contains(array[i])) {
					fastRemove(i--);
					length--;
				}

		} finally {
			writeUnlock();
		}

		return true;
	}

	@Override
	public final E search(E required, Search<E> search) {
		readLock();
		try {

			E[] array = array();

			for(int i = 0, length = size; i < length; i++) {

				E element = array[i];

				if(search.compare(required, element)) {
					return element;
				}
			}

			return null;

		} finally {
			readUnlock();
		}
	}

	@Override
	public final void set(int index, E element) {

		if(index < 0 || index >= size || element == null) {
			return;
		}

		writeLock();
		try {

			E[] array = array();

			if(array[index] != null) {
				size -= 1;
			}

			array[index] = element;

			size += 1;

		} finally {
			writeUnlock();
		}
	}

	@Override
	protected final void setArray(E[] array) {
		this.array = array;
	}

	@Override
	protected final void setSize(int size) {
		this.size = size;
	}

	@Override
	public final int size() {
		return size;
	}

	@Override
	public final E slowRemove(int index) {

		if(index < 0 || size < 1) {
			return null;
		}

		writeLock();
		try {

			E[] array = array();

			int numMoved = size - index - 1;

			E old = array[index];

			if(numMoved > 0) {
				System.arraycopy(array, index + 1, array, index, numMoved);
			}

			size -= 1;

			array[size] = null;

			return old;
		} finally {
			writeUnlock();
		}
	}

	@Override
	public final ConcurrentArray<E> sort(Comparator<E> comparator) {
		writeLock();
		try {
			Arrays.sort(array, comparator);
			return this;
		} finally {
			writeUnlock();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <T> T[] toArray(T[] container) {
		readLock();
		try {

			E[] array = array();

			if(container.length >= size) {

				for(int i = 0, j = 0, length = array.length, newLength = container.length; i < length && j < newLength; i++) {

					if(array[i] == null) {
						continue;
					}

					container[j++] = (T) array[i];
				}

				return container;
			}

			return (T[]) array;
		} finally {
			readUnlock();
		}
	}

	@Override
	public final ConcurrentArray<E> trimToSize() {

		if(size == array.length) {
			return this;
		}

		writeLock();
		try {
			array = Arrays.copyOfRange(array, 0, size);
			return this;
		} finally {
			writeUnlock();
		}
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