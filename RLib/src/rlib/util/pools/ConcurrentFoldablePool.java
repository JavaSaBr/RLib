package rlib.util.pools;

import rlib.util.array.Array;
import rlib.util.array.ArrayUtils;

/**
 * Потокобезопасный объектный пул.
 * 
 * @author Ronn
 */
public class ConcurrentFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** массив объектов */
	private final Array<E> pool;

	protected ConcurrentFoldablePool(int size, Class<?> type) {
		this.pool = ArrayUtils.toConcurrentArray(type, size);
	}

	@Override
	public boolean isEmpty() {
		return pool.isEmpty();
	}

	@Override
	public void put(E object) {

		if(object == null) {
			return;
		}

		object.finalyze();

		Array<E> pool = getPool();
		pool.writeLock();
		try {
			pool.add(object);
		} finally {
			pool.writeUnlock();
		}
	}

	/**
	 * @return массив объектов.
	 */
	private Array<E> getPool() {
		return pool;
	}

	@Override
	public void remove(E object) {
		Array<E> pool = getPool();
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

		Array<E> pool = getPool();
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
