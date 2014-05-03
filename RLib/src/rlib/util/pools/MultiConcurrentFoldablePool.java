package rlib.util.pools;

/**
 * Составной потокобезопасный пул.
 * 
 * @author Ronn
 */
public final class MultiConcurrentFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** массив пулов */
	private final ConcurrentFoldablePool<E>[] pools;

	/** кол-во пулов */
	private final int limit;

	/** индекс следующего */
	private int order;

	@SuppressWarnings("unchecked")
	protected MultiConcurrentFoldablePool(final int size, final Class<?> type) {
		this.pools = new ConcurrentFoldablePool[size];
		this.limit = size;

		for(int i = 0; i < size; i++) {
			pools[i] = new ConcurrentFoldablePool<E>(10, type);
		}
	}

	/**
	 * @return номер пула.
	 */
	private final int getNextOrder() {

		int next = order + 1;

		if(next >= limit) {
			next = 0;
		}

		setOrder(next);
		return next;
	}

	/**
	 * @return номер пула.
	 */
	private final int getOrder() {

		int next = order;

		if(next >= limit) {
			next = 0;
		}

		setOrder(next);
		return next;
	}

	/**
	 * @return массив пулов.
	 */
	private final ConcurrentFoldablePool<E>[] getPools() {
		return pools;
	}

	@Override
	public boolean isEmpty() {

		final ConcurrentFoldablePool<E>[] pools = getPools();

		// если какой-то пул не пуст, возвращаем фалс
		for(int i = 0, length = pools.length; i < length; i++) {
			if(!pools[i].isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void put(final E object) {

		if(object == null) {
			return;
		}

		object.finalyze();
		pools[getOrder()].put(object);
	}

	@Override
	public void remove(final E object) {
		throw new RuntimeException("unsupperted method.");
	}

	/**
	 * @param order номер пула.
	 */
	private final void setOrder(final int order) {
		this.order = order;
	}

	@Override
	public E take() {

		final E object = pools[getNextOrder()].take();

		if(object == null) {
			return null;
		}

		object.reinit();
		return object;
	}
}
