package rlib.util.array;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;

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
	public FastArray(Class<E> type) {
		super(type);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	public FastArray(Class<E> type, int size) {
		super(type, size);
	}

	@Override
	public void accept(Consumer<? super E> consumer) {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			consumer.accept(array[i]);
		}
	}

	@Override
	public FastArray<E> add(E element) {

		if(size == array.length) {
			array = Arrays.copyOf(array, array.length * 3 / 2 + 1);
		}

		array[size++] = element;

		return this;
	}

	@Override
	public final FastArray<E> addAll(Array<? extends E> elements) {

		if(elements == null || elements.isEmpty()) {
			return this;
		}

		int diff = size + elements.size() - array.length;

		if(diff > 0) {
			array = Arrays.copyOf(array, diff);
		}

		E[] array = elements.array();

		for(int i = 0, length = elements.size(); i < length; i++) {
			add(array[i]);
		}

		return this;
	}

	@Override
	public final Array<E> addAll(E[] elements) {

		if(elements == null || elements.length < 1) {
			return this;
		}

		int diff = size + elements.length - array.length;

		if(diff > 0) {
			array = Arrays.copyOf(array, diff);
		}

		for(int i = 0, length = elements.length; i < length; i++) {
			add(elements[i]);
		}

		return this;
	}

	@Override
	public void apply(Function<? super E, ? extends E> function) {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			array[i] = function.apply(array[i]);
		}
	}

	@Override
	public final E[] array() {
		return array;
	}

	@Override
	public final FastArray<E> clear() {

		Arrays.clear(array);

		size = 0;

		return this;
	}

	@Override
	public final boolean contains(Object object) {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {
			if(array[i].equals(object)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public final E fastRemove(int index) {

		E[] array = array();

		if(index < 0 || size < 1 || index >= size) {
			return null;
		}

		size -= 1;

		E old = array[index];

		array[index] = array[size];
		array[size] = null;

		return old;
	}

	@Override
	public final E first() {

		if(size < 1) {
			return null;
		}

		return array[0];
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

		E[] array = array();

		for(int i = 0, length = size; i < length; i++) {

			E element = array[i];

			if(element.equals(object)) {
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
	public final ArrayIterator<E> iterator() {
		return new FastIterator();
	}

	@Override
	public final E last() {

		if(size < 1) {
			return null;
		}

		return array[size - 1];
	}

	@Override
	public final int lastIndexOf(Object object) {

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
	public final E poll() {
		return slowRemove(0);
	}

	@Override
	public final E pop() {
		return fastRemove(size - 1);
	}

	@Override
	public final boolean removeAll(Array<?> target) {

		if(target.isEmpty()) {
			return true;
		}

		Object[] array = target.array();

		for(int i = 0, length = target.size(); i < length; i++) {
			fastRemove(array[i]);
		}

		return true;
	}

	@Override
	public final boolean retainAll(Array<?> target) {

		E[] array = array();

		for(int i = 0, length = size; i < length; i++)
			if(!target.contains(array[i])) {
				fastRemove(i--);
				length--;
			}

		return true;
	}

	@Override
	public final E search(E required, Search<E> search) {

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
	public final void set(int index, E element) {

		if(index < 0 || index >= size || element == null) {
			return;
		}

		E[] array = array();

		if(array[index] != null) {
			size -= 1;
		}

		array[index] = element;

		size += 1;
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

		E[] array = array();

		int numMoved = size - index - 1;

		E old = array[index];

		if(numMoved > 0) {
			System.arraycopy(array, index + 1, array, index, numMoved);
		}

		size -= 1;

		array[size] = null;

		return old;
	}

	@Override
	public final FastArray<E> sort(Comparator<E> comparator) {
		Arrays.sort(array, comparator);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <T> T[] toArray(T[] container) {

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
	}

	@Override
	public final FastArray<E> trimToSize() {

		if(size == array.length) {
			return this;
		}

		array = Arrays.copyOfRange(array, 0, size);

		return this;
	}
}