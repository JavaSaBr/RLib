package com.ss.rlib.common.util.pools.impl;

import com.ss.rlib.common.util.array.impl.ConcurrentReentrantRWLockArray;
import com.ss.rlib.common.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import com.ss.rlib.common.util.pools.Reusable;

/**
 * The threadsafe implementation of the {@link ReusablePool} using like a storage the {@link
 * ConcurrentReentrantRWLockArray}*.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentReentrantRWLockPool<E extends Reusable> extends ConcurrentReusablePool<E> {

    public ConcurrentReentrantRWLockPool(@NotNull Class<? super E> type) {
        super(type);
    }

    @Override
    protected @NotNull ConcurrentArray<E> createPool(@NotNull Class<? super E> type) {
        return ArrayFactory.newConcurrentReentrantRWLockArray(type);
    }
}
