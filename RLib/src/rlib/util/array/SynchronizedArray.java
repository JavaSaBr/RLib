package rlib.util.array;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Массив с синхронизированными методами.
 *
 * @author Ronn
 */
public final class SynchronizedArray<E> extends AbstractArray<E> {

	/**
	 * Быстрый итератор массива.
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
			SynchronizedArray.this.fastRemove(--ordinal);
		}

		@Override
		public void slowRemove() {
			SynchronizedArray.this.slowRemove(--ordinal);
		}
	}

	private static final long serialVersionUID = -8477384427415127978L;

	/** массив элементов */
	private volatile E[] array;

	/** кол-во элементов в колекции */
	private volatile int size;

	/**
	 * @param type тип элементов в массиве.
	 */
	public SynchronizedArray(Class<E> type) {
		super(type);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	public SynchronizedArray(Class<E> type, int size) {
		super(type, size);
	}

	@Override
	public synchronized void accept(Consumer<? super E> consumer) {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			consumer.accept(array[i]);
		}
	}

	@Override
	public synchronized SynchronizedArray<E> add(E element) {

		if(size == array.length) {
			array = Arrays.copyOf(array, array.length * 3 / 2 + 1);
		}

		array[size++] = element;

		return this;
	}

	@Override
	public synchronized SynchronizedArray<E> addAll(Array<? extends E> addArray) {

		if(addArray == null || addArray.isEmpty()) {
			return this;
		}

		int diff = size + addArray.size() - array.length;

		if(diff > 0) {
			array = Arrays.copyOf(array, diff);
		}

		E[] array = addArray.array();

		for(int i = 0, length = addArray.size(); i < length; i++) {
			add(array[i]);
		}

		return this;
	}

	@Override
	public synchronized Array<E> addAll(E[] addArray) {

		if(addArray == null || addArray.length < 1) {
			return this;
		}

		int diff = size + addArray.length - array.length;

		if(diff > 0) {
			array = Arrays.copyOf(array, diff);
		}

		for(int i = 0, length = addArray.length; i < length; i++) {
			add(addArray[i]);
		}

		return this;
	}

	@Override
	public synchronized void apply(Function<? super E, ? extends E> function) {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			array[i] = function.apply(array[i]);
		}
	}

	@Override
	public E[] array() {
		return array;
	}

	@Override
	public synchronized SynchronizedArray<E> clear() {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			array[i] = null;
		}

		size = 0;

		return this;
	}

	@Override
	public synchronized boolean contains(Object object) {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			if(array[i].equals(object)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public synchronized E fastRemove(int index) {

		if(index < 0 || size < 1) {
			return null;
		}

		E old = array[index];

		array[index] = array[--size];
		array[size] = null;

		return old;
	}

	@Override
	public synchronized E first() {

		if(size < 1) {
			return null;
		}

		return array[0];
	}

	@Override
	public E get(int index) {
		return array[index];
	}

	@Override
	public synchronized int indexOf(Object object) {

		if(object == null) {
			return -1;
		}

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			if(array[i].equals(object)) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public boolean isEmpty() {
		return size < 1;
	}

	@Override
	public ArrayIterator<E> iterator() {
		return new FastIterator();
	}

	@Override
	public synchronized E last() {

		if(size < 1) {
			return null;
		}

		return array[size - 1];
	}

	@Override
	public synchronized int lastIndexOf(Object object) {

		if(object == null) {
			return -1;
		}

		E[] array = array();

		int last = -1;

		for(int i = 0, length = size; i < length; i++) {

			E element = array[i];

			if(element.equals(object)) {
				last = i;
			}
		}

		return last;
	}

	@Override
	public E poll() {
		return slowRemove(0);
	}

	@Override
	public E pop() {
		return fastRemove(size - 1);
	}

	@Override
	public synchronized boolean removeAll(Array<?> remove) {

		if(remove.isEmpty()) {
			return true;
		}

		Object[] array = remove.array();

		for(int i = 0, length = remove.size(); i < length; i++) {
			fastRemove(array[i]);
		}

		return true;
	}

	@Override
	public synchronized boolean retainAll(Array<?> targetArray) {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			if(!targetArray.contains(array[i])) {
				fastRemove(i--);
			}
		}

		return true;
	}

	@Override
	public synchronized E search(E required, Search<E> search) {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {

			E element = array[i];

			if(search.compare(required, element)) {
				return element;
			}
		}

		return null;
	}

	@Override
	public synchronized void set(int index, E element) {

		if(index < size || element == null) {
			return;
		}

		if(array[index] != null) {
			size--;
		}

		array[index] = element;

		size++;
	}

	@Override
	protected void setArray(E[] array) {
		this.array = array;
	}

	@Override
	protected void setSize(int size) {
		this.size = size;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public synchronized E slowRemove(int index) {

		if(index < 0 || size < 1) {
			return null;
		}

		int numMoved = size - index - 1;

		E old = array[index];

		if(numMoved > 0) {
			System.arraycopy(array, index + 1, array, index, numMoved);
		}

		array[--size] = null;

		return old;
	}

	@Override
	public synchronized SynchronizedArray<E> sort(Comparator<E> comparator) {
		Arrays.sort(array, comparator);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T[] toArray(T[] newArray) {

		if(newArray.length >= size) {

			for(int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {

				if(array[i] == null) {
					continue;
				}

				newArray[j++] = (T) array[i];
			}

			return newArray;
		}

		return (T[]) array;
	}

	@Override
	public synchronized SynchronizedArray<E> trimToSize() {

		if(size == array.length) {
			return this;
		}

		array = Arrays.copyOfRange(array, 0, size);

		return this;
	}
}