package com.ss.rlib.util.pools.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.array.ConcurrentArray;
import com.ss.rlib.util.array.impl.ConcurrentReentrantRWLockArray;
import com.ss.rlib.util.pools.Reusable;
import com.ss.rlib.util.pools.ReusablePool;

import static com.ss.rlib.util.ArrayUtils.getInWriteLock;
import static com.ss.rlib.util.ArrayUtils.runInWriteLock;

/**
 * The threadsafe implementation of the {@link ReusablePool} using like a storage the {@link
 * ConcurrentReentrantRWLockArray}*.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentReentrantRWLockPool<E extends Reusable> implements ReusablePool<E> {

    /**
     * The storage of objects.
     */
    private final ConcurrentArray<E> pool;

    /**
     * Instantiates a new Concurrent reentrant rw lock pool.
     *
     * @param type the type
     */
    public ConcurrentReentrantRWLockPool(final Class<?> type) {
        this.pool = ArrayFactory.newConcurrentReentrantRWLockArray(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(@NotNull final E object) {
        object.free();
        runInWriteLock(pool, object, Array::add);
    }

    @Override
    public void remove(@NotNull final E object) {
        runInWriteLock(pool, object, Array::fastRemove);
    }

    @Nullable
    @Override
    public E take() {

        if (pool.isEmpty()) return null;

        final E object = getInWriteLock(pool, Array::pop);
        if (object == null) return null;

        object.reuse();

        return object;
    }
}
