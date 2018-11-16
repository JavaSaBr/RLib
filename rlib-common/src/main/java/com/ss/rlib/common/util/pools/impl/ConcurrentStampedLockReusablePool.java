package com.ss.rlib.common.util.pools.impl;

import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import com.ss.rlib.common.util.array.impl.ConcurrentStampedLockArray;
import com.ss.rlib.common.util.pools.Reusable;
import com.ss.rlib.common.util.pools.ReusablePool;

/**
 * The threadsafe implementation of the {@link ReusablePool} using like a storage the {@link
 * ConcurrentStampedLockArray}*.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentStampedLockReusablePool<E extends Reusable> extends ConcurrentReusablePool<E> {

    public ConcurrentStampedLockReusablePool(@NotNull Class<? super E> type) {
        super(type);
    }

    @Override
    protected @NotNull ConcurrentArray<E> createPool(@NotNull Class<? super E> type) {
        return ArrayFactory.newConcurrentStampedLockArray(type);
    }
}
