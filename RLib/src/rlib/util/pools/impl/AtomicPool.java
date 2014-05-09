package rlib.util.pools.impl;

import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;
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
	/** блокировщик */
	private final Lock lock;

	public AtomicPool(final Class<?> type) {
		this.pool = ArrayFactory.newArray(type);
		this.lock = LockFactory.newPrimitiveAtomicLock();
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

		lock.lock();
		try {
			pool.add(object);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void remove(final E object) {
		lock.lock();
		try {
			pool.fastRemove(object);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public E take() {

		E object = null;

		lock.lock();
		try {
			object = pool.pop();
		} finally {
			lock.unlock();
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
