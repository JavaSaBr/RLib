package com.ss.rlib.common.util.pools.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.pools.Reusable;
import com.ss.rlib.common.util.pools.ReusablePool;

/**
 * The implementation of the {@link ReusablePool} using synchronization for take/put methods.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class SynchronizedReusablePool<E extends Reusable> extends FastReusablePool<E> {

    public SynchronizedReusablePool(@NotNull Class<? super E> type) {
        super(type);
    }

    @Override
    public synchronized boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public synchronized void put(@NotNull E object) {
        super.put(object);
    }

    @Override
    public synchronized void remove(@NotNull E object) {
        super.remove(object);
    }

    @Override
    public synchronized @Nullable E take() {
        return super.take();
    }
}
