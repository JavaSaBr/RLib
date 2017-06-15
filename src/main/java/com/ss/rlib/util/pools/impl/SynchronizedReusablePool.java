package com.ss.rlib.util.pools.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.pools.Reusable;
import com.ss.rlib.util.pools.ReusablePool;

/**
 * The implementation of the {@link ReusablePool} using synchronization for take/put methods.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class SynchronizedReusablePool<E extends Reusable> implements ReusablePool<E> {

    /**
     * The storage of objects.
     */
    private final Array<E> pool;

    /**
     * Instantiates a new Synchronized reusable pool.
     *
     * @param type the type
     */
    public SynchronizedReusablePool(final Class<?> type) {
        this.pool = ArrayFactory.newArray(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(@NotNull final E object) {
        object.free();
        ArrayUtils.addInSynchronizeTo(pool, object);
    }

    @Override
    public void remove(@NotNull final E object) {
        ArrayUtils.fastRemoveInSynchronizeTo(pool, object);
    }

    @Nullable
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
