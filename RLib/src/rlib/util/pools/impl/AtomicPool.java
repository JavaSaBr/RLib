package rlib.util.pools.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Pool;

/**
 * Реализация потокобезопасного {@link Pool} с помощью атомарного блокировщика.
 * 
 * @author Ronn
 */
public class AtomicPool<E> implements Pool<E> {

	/** пул объектов */
	private final Array<E> pool;

	public AtomicPool(final Class<?> type) {
		this.pool = ArrayFactory.newConcurrentAtomicArray(type);
	}

	/**
	 * @return пул объектов.
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

		return object;
	}

	@Override
	public String toString() {
		return pool.toString();
	}
}
