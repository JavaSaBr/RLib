package rlib.util.pools.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.array.ConcurrentArray;
import rlib.util.array.impl.ConcurrentReentrantRWLockArray;
import rlib.util.pools.Reusable;
import rlib.util.pools.ReusablePool;

import static rlib.util.ArrayUtils.getInWriteLock;
import static rlib.util.ArrayUtils.runInWriteLock;

/**
 * The threadsafe implementation of the {@link ReusablePool} using like a storage the {@link
 * ConcurrentReentrantRWLockArray}.
 *
 * @author JavaSaBr
 */
public class ConcurrentReentrantRWLockPool<E extends Reusable> implements ReusablePool<E> {

    /**
     * The storage of objects.
     */
    private final ConcurrentArray<E> pool;

    public ConcurrentReentrantRWLockPool(final Class<?> type) {
        this.pool = ArrayFactory.newConcurrentReentrantRWLockArray(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(final E object) {
        if (object == null) return;
        object.free();
        runInWriteLock(pool, object, Array::add);
    }

    @Override
    public void remove(final E object) {
        runInWriteLock(pool, object, Array::fastRemove);
    }

    @Override
    public E take() {

        if (pool.isEmpty()) return null;

        final E object = getInWriteLock(pool, Array::pop);
        if (object == null) return null;

        object.reuse();

        return object;
    }
}
