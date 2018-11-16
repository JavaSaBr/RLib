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
    @NotNull
    private final Array<E> pool;

    public FastPool(@NotNull Class<? super E> type) {
        this.pool = Array.ofType(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(@NotNull E object) {
        pool.add(object);
    }

    @Override
    public void remove(@NotNull E object) {
        pool.fastRemove(object);
    }

    @Override
    public @Nullable E take() {

        E object = pool.pop();

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
