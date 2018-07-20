package com.ss.rlib.common.util.pools.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.pools.Reusable;
import com.ss.rlib.common.util.pools.ReusablePool;

/**
 * The fast implementation of the {@link ReusablePool}. It isn't threadsafe.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FastReusablePool<E extends Reusable> extends FastPool<E> implements ReusablePool<E> {

    public FastReusablePool(@NotNull Class<? super E> type) {
        super(type);
    }

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
