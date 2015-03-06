package rlib.util.pools.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;

/**
 * Реализация потокобезопасного {@link FoldablePool} с помощью атомарного
 * блокировщика.
 * 
 * @author Ronn
 */
public class AtomicFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** контейнер объектов */
	private final Array<E> pool;

	public AtomicFoldablePool(final Class<?> type) {
		this.pool = ArrayFactory.newConcurrentAtomicArray(type);
	}

	@Override
	public boolean isEmpty() {
		return pool.isEmpty();
	}

	/**
	 * @return контейнер объектов.
	 */
	private Array<E> getPool() {
		return pool;
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

		final Array<E> pool = getPool();

		if(pool.isEmpty()) {
			return null;
		}

		E object = null;

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

	@Override
	public String toString() {
		return pool.toString();
	}
}
