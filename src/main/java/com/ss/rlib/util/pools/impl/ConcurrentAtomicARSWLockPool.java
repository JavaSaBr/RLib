package com.ss.rlib.util.pools.impl;

import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.array.ConcurrentArray;
import com.ss.rlib.util.array.impl.ConcurrentAtomicARSWLockArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ss.rlib.util.pools.Pool;

import static com.ss.rlib.util.ArrayUtils.getInWriteLock;
import static com.ss.rlib.util.ArrayUtils.runInWriteLock;

/**
 * The threadsafe implementation of the {@link Pool} using like a storage the {@link
 * ConcurrentAtomicARSWLockArray}*.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockPool<E> implements Pool<E> {

    /**
     * The storage of objects.
     */
    private final ConcurrentArray<E> pool;

    /**
     * Instantiates a new Concurrent atomic arsw lock pool.
     *
     * @param type the type
     */
    public ConcurrentAtomicARSWLockPool(final Class<?> type) {
        this.pool = ArrayFactory.newConcurrentAtomicARSWLockArray(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(@NotNull final E object) {
        ArrayUtils.runInWriteLock(pool, object, Array::add);
    }

    @Override
    public void remove(@NotNull final E object) {
        ArrayUtils.runInWriteLock(pool, object, Array::fastRemove);
    }

    @Nullable
    @Override
    public E take() {

        if (pool.isEmpty()) return null;

        final E object = ArrayUtils.getInWriteLock(pool, Array::pop);
        if (object == null) return null;

        return object;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
