package rlib.util.array;

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
	public FastArray<E> add(E element) {

		if(size == array.length) {
			array = ArrayUtils.copyOf(array, array.length * 3 / 2 + 1);
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
			array = ArrayUtils.copyOf(array, diff);
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
			array = ArrayUtils.copyOf(array, diff);
		}

		for(int i = 0, length = elements.length; i < length; i++) {
			add(elements[i]);
		}

		return this;
	}

	@Override
	public final E[] array() {
		return array;
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
	public final E get(int index) {
		return array[index];
	}

	@Override
	public final ArrayIterator<E> iterator() {
		return new FastIterator();
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
	public final FastArray<E> trimToSize() {

		if(size == array.length) {
			return this;
		}

		array = ArrayUtils.copyOfRange(array, 0, size);

		return this;
	}
}