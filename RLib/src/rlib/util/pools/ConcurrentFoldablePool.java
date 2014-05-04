package rlib.util.pools;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Потокобезопасный объектный пул.
 * 
 * @author Ronn
 */
public class ConcurrentFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** массив объектов */
	private final Array<E> pool;

	protected ConcurrentFoldablePool(final Class<?> type) {
		this.pool = ArrayFactory.newConcurrentArray(type);
	}

	/**
	 * @return массив объектов.
	 */
	private Array<E> getPool() {
		return pool;
	}

	@Override
	public boolean isEmpty() {
		return pool.isEmpty();
	}

	@Override
	public void put(final E object) {

		if(object == null) {
			return;
		}

		object.finalyze();

		final Array<E> pool = getPool();
		pool.writeLock();
		try {
			pool.add(object);
		} finally {
			pool.writeUnlock();
		}
	}

	@Override
	public void remove(final E object) {
		final Array<E> pool = getPool();
		pool.writeLock();
		try {
			pool.fastRemove(object);
		} finally {
			pool.writeUnlock();
		}
	}

	@Override
	public E take() {

		E object = null;

		final Array<E> pool = getPool();
		pool.writeLock();
		try {
			object = pool.pop();
		} finally {
			pool.writeUnlock();
		}

		if(object == null) {
			return null;
		}

		object.reinit();
		return object;
	}
}
