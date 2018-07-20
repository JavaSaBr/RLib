package com.ss.rlib.common.util.pools.impl;

import com.ss.rlib.common.util.pools.Pool;
import com.ss.rlib.common.util.pools.Reusable;
import com.ss.rlib.common.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base concurrent implementation of the {@link Pool} for {@link com.ss.rlib.common.util.pools.Reusable} objects.
 *
 * @param <E> the object's type.
 * @author JavaSaBr
 */
public abstract class ConcurrentReusablePool<E extends Reusable> extends ConcurrentPool<E>
        implements ReusablePool<E> {

    public ConcurrentReusablePool(@NotNull Class<? super E> type) {
        super(type);
    }

    @Override
    public void put(@NotNull E object) {
        object.free();
        super.put(object);
    }

    @Override
    public @Nullable E take() {

        E object = super.take();

        if (object != null) {
            object.reuse();
        }

        return object;
    }
}
