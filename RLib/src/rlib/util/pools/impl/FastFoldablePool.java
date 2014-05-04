package rlib.util.pools.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;

/**
 * Реализация не потокобезопасного легковесного {@link FoldablePool}.
 * 
 * @author Ronn
 */
public class FastFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** пул объектов */
	private final Array<E> pool;

	public FastFoldablePool(final Class<?> type) {
		this.pool = ArrayFactory.newArray(type);
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
		pool.add(object);
	}

	@Override
	public void remove(final E object) {
		pool.fastRemove(object);
	}

	@Override
	public E take() {

		final E object = pool.pop();

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
