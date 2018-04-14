package com.ss.rlib.common.util.pools.impl;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ss.rlib.common.util.pools.Pool;

/**
 * The fast implementation of the {@link Pool}. It isn't threadsafe.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FastPool<E> implements Pool<E> {

    /**
     * The storage of objects.
     */
    private final Array<E> pool;

    /**
     * Instantiates a new Fast pool.
     *
     * @param type the type
     */
    public FastPool(final Class<?> type) {
        this.pool = ArrayFactory.newArray(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(@NotNull final E object) {
        pool.add(object);
    }

    @Override
    public void remove(@NotNull final E object) {
        pool.fastRemove(object);
    }

    @Nullable
    @Override
    public E take() {
        final E object = pool.pop();
        if (object == null) return null;
        return object;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
