package com.ss.rlib.common.util.pools.impl;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import com.ss.rlib.common.util.array.impl.ConcurrentAtomicARSWLockArray;
import com.ss.rlib.common.util.pools.Pool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base concurrent implementation of the {@link Pool}.
 *
 * @param <E> the object's type.
 * @author JavaSaBr
 */
public abstract class ConcurrentPool<E> implements Pool<E> {

    /**
     * The storage of objects.
     */
    @NotNull
    protected final ConcurrentArray<E> pool;

    public ConcurrentPool(@NotNull Class<? super E> type) {
        this.pool = createPool(type);
    }

    protected abstract @NotNull ConcurrentArray<E> createPool(@NotNull Class<? super E> type);

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(@NotNull E object) {
        pool.runInWriteLock(object, Array::add);
    }

    @Override
    public void remove(@NotNull E object) {
        pool.runInWriteLock(object, Array::fastRemove);
    }

    @Override
    public @Nullable E take() {

        if (pool.isEmpty()) {
            return null;
        }

        E object = pool.getInWriteLock(Array::pop);

        if (object == null) {
            return null;
        }

        return object;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
