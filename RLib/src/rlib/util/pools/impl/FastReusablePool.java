package rlib.util.pools.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Reusable;
import rlib.util.pools.ReusablePool;

/**
 * The fast implementation of the {@link ReusablePool}. It isn't threadsafe.
 *
 * @author JavaSaBr
 */
public class FastReusablePool<E extends Reusable> implements ReusablePool<E> {

    /**
     * The storage of objects.
     */
    private final Array<E> pool;

    public FastReusablePool(final Class<?> type) {
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
        pool.add(object);
    }

    @Override
    public void remove(final E object) {
        pool.fastRemove(object);
    }

    @Override
    public E take() {

        final E object = pool.pop();
        if (object == null) return null;

        object.reuse();

        return object;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
