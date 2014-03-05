package rlib.util.array;

/**
 * Базовая реализация массивов.
 *
 * @author Ronn
 */
public abstract class AbstractArray<E> implements Array<E> {

	private static final long serialVersionUID = 2113052245369887690L;

	/** размер массива по умолчанию */
	protected static final int DEFAULT_SIZE = 10;

	/**
	 * @param type тип элементов в массиве.
	 */
	public AbstractArray(Class<E> type) {
		this(type, DEFAULT_SIZE);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	@SuppressWarnings("unchecked")
	public AbstractArray(Class<E> type, int size) {
		super();

		if(size < 0) {
			throw new IllegalArgumentException("negative size");
		}

		setSize(0);
		setArray((E[]) java.lang.reflect.Array.newInstance(type, size));
	}

	@Override
	public final boolean containsAll(Array<?> array) {

		Object[] elements = array.array();

		for(int i = 0, length = array.size(); i < length; i++) {
			if(!contains(elements[i])) {
				return false;
			}
		}

		return true;
	}

	@Override
	public final boolean containsAll(Object[] array) {

		for(int i = 0, length = array.length; i < length; i++) {
			if(!contains(array[i])) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean fastRemove(Object object) {
		return fastRemove(indexOf(object)) != null;
	}

	@Override
	public void finalyze() {
		clear();
	}

	/**
	 * @param array массив элементов.
	 */
	protected abstract void setArray(E[] array);

	/**
	 * @param size размер массива.
	 */
	protected abstract void setSize(int size);

	@Override
	public final boolean slowRemove(Object object) {
		return slowRemove(indexOf(object)) != null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " size = " + size() + " : " + Arrays.toString(this);
	}
}
