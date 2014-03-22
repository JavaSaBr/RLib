package rlib.util.pools;

import rlib.util.array.Array;
import rlib.util.array.ArrayUtils;

/**
 * Не потокобезопасный пул.
 * 
 * @author Ronn
 */
public class FastFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** пул объектов */
	private final Array<E> pool;

	protected FastFoldablePool(int size, Class<?> type) {
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
		pool.add(object);
	}

	@Override
	public void remove(E object) {
		pool.fastRemove(object);
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
	public String toString() {
		return pool.toString();
	}
}
