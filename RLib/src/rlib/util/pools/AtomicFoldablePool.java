package rlib.util.pools;

import java.util.concurrent.locks.Lock;

import rlib.concurrent.lock.LockFactory;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Реализация потокобезопасного {@link FoldablePool} с помощью атомарного
 * блокировщика.
 * 
 * @author Ronn
 */
public class AtomicFoldablePool<E extends Foldable> implements FoldablePool<E> {

	/** пул объектов */
	private final Array<E> pool;
	/** блокировщик */
	private final Lock lock;

	protected AtomicFoldablePool(final Class<?> type) {
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

		object.finalyze();

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

		object.reinit();
		return object;
	}

	@Override
	public String toString() {
		return pool.toString();
	}
}
