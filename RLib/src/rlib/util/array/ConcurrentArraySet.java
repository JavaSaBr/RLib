package rlib.util.array;

/**
 * Модель уникального множества на основе конкурентного массива.
 * 
 * @author Ronn
 */
public class ConcurrentArraySet<E> extends ConcurrentArray<E> {

	private static final long serialVersionUID = 1L;

	public ConcurrentArraySet(Class<E> type) {
		super(type);
	}

	public ConcurrentArraySet(Class<E> type, int size) {
		super(type, size);
	}

	@Override
	public ConcurrentArray<E> add(E element) {

		if(contains(element)) {
			return this;
		}

		return super.add(element);
	}
}
