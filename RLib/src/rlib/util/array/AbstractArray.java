package rlib.util.array;

import rlib.util.ArrayUtils;

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
	public AbstractArray(final Class<E> type) {
		this(type, DEFAULT_SIZE);
	}

	/**
	 * @param type тип элементов в массиве.
	 * @param size размер массива.
	 */
	@SuppressWarnings("unchecked")
	public AbstractArray(final Class<E> type, final int size) {
		super();

		if(size < 0) {
			throw new IllegalArgumentException("negative size");
		}

		setArray((E[]) java.lang.reflect.Array.newInstance(type, size));
	}

	@Override
	public Array<E> clear() {

		if(size() > 0) {
			ArrayUtils.clear(array());
			setSize(0);
		}

		return this;
	}

	@Override
	public final void finalyze() {
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
	public final boolean slowRemove(final Object object) {
		return slowRemove(indexOf(object)) != null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " size = " + size() + " : " + ArrayUtils.toString(this);
	}
}
