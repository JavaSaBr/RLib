package rlib.util.pools;

import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * Потокобезопасный объектный пул.
 * 
 * @author Ronn
 */
public class ConcurrentFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** массив объектов */
	private final Array<E> pool;

	protected ConcurrentFoldablePool(int size, Class<?> type) {
		this.pool = Arrays.toConcurrentArray(type, size);
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
		pool.add(object);
	}

	@Override
	public E take() {

		E object = pool.pop();

		if(object == null) {
			return null;
		}

		object.reinit();
		return object;
	}

	@Override
	public void remove(E object) {
		pool.fastRemove(object);
	}
}
