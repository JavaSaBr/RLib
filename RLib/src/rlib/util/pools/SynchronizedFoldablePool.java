package rlib.util.pools;

import rlib.util.array.Array;
import rlib.util.array.ArrayUtils;

/**
 * Синхронизированный объектный пул.
 * 
 * @author Ronn
 */
public class SynchronizedFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** пул объектов */
	private final Array<E> pool;

	protected SynchronizedFoldablePool(int size, Class<?> type) {
		this.pool = ArrayUtils.toArray(type, size);
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

		synchronized(pool) {
			pool.add(object);
		}
	}

	@Override
	public void remove(E object) {
		synchronized(pool) {
			pool.fastRemove(object);
		}
	}

	@Override
	public E take() {

		E object = null;

		synchronized(pool) {
			object = pool.pop();
		}

		if(object == null) {
			return null;
		}

		object.reinit();
		return object;
	}

	@Override
	public String toString() {
		return pool.toString();
	}
}
