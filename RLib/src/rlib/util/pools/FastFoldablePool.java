package rlib.util.pools;

import rlib.util.array.Array;
import rlib.util.array.Arrays;

/**
 * Не потокобезопасный пул.
 * 
 * @author Ronn
 */
public class FastFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** пул объектов */
	private final Array<E> pool;

	protected FastFoldablePool(int size, Class<?> type) {
		this.pool = Arrays.toArray(type, size);
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
	public String toString() {
		return pool.toString();
	}

	@Override
	public void remove(E object) {
		pool.fastRemove(object);
	}
}
