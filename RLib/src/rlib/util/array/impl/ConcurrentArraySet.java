package rlib.util.array.impl;

/**
 * Модель уникального множества на основе конкурентного массива.
 * 
 * @author Ronn
 */
public class ConcurrentArraySet<E> extends ConcurrentArray<E> {

	private static final long serialVersionUID = 1L;

	public ConcurrentArraySet(final Class<E> type) {
		super(type);
	}

	public ConcurrentArraySet(final Class<E> type, final int size) {
		super(type, size);
	}

	@Override
	public ConcurrentArray<E> add(final E element) {

		if(contains(element)) {
			return this;
		}

		return super.add(element);
	}
}
