package rlib.util.pools.impl;

import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Reusable;
import rlib.util.pools.ReusablePool;

/**
 * The implementation of the {@link ReusablePool} using synchronization for take/put methods.
 *
 * @author JavaSaBr
 */
public class SynchronizedReusablePool<E extends Reusable> implements ReusablePool<E> {

    /**
     * The storage of objects.
     */
    private final Array<E> pool;

    public SynchronizedReusablePool(final Class<?> type) {
        this.pool = ArrayFactory.newArray(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(final E object) {
        if (object == null) return;
        object.free();
        ArrayUtils.addInSynchronizeTo(pool, object);
    }

    @Override
    public void remove(final E object) {
        ArrayUtils.fastRemoveInSynchronizeTo(pool, object);
    }

    @Override
    public E take() {

        E object;

        synchronized (pool) {
            object = pool.pop();
        }

        if (object == null) return null;

        object.reuse();

        return object;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
