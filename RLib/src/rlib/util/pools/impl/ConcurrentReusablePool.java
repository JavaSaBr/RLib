package rlib.util.pools.impl;

import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.array.impl.ConcurrentArray;
import rlib.util.pools.Reusable;
import rlib.util.pools.ReusablePool;

/**
 * Реализация потокобезопасного {@link ReusablePool} с помощью потокобезопасного массива {@link
 * ConcurrentArray}
 *
 * @author Ronn
 */
public class ConcurrentReusablePool<E extends Reusable> implements ReusablePool<E> {

    /**
     * Пул объектов.
     */
    private final Array<E> pool;

    public ConcurrentReusablePool(final Class<?> type) {
        this.pool = ArrayFactory.newConcurrentArray(type);
    }

    /**
     * @return массив объектов.
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

        if (object == null) {
            return;
        }

        object.free();

        ArrayUtils.addInWriteLockTo(pool, object);
    }

    @Override
    public void remove(final E object) {
        ArrayUtils.fastRemoveInWriteLockTo(pool, object);
    }

    @Override
    public E take() {

        final Array<E> pool = getPool();

        if (pool.isEmpty()) {
            return null;
        }

        E object = null;

        pool.writeLock();
        try {
            object = pool.pop();
        } finally {
            pool.writeUnlock();
        }

        if (object == null) {
            return null;
        }

        object.reuse();

        return object;
    }
}
