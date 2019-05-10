package com.ss.rlib.common.util.pools.impl;

import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import com.ss.rlib.common.util.array.impl.ConcurrentStampedLockArray;
import com.ss.rlib.common.util.pools.Pool;
import org.jetbrains.annotations.NotNull;

/**
 * The threadsafe implementation of the {@link Pool} using like a storage the {@link
 * ConcurrentStampedLockArray}*.
 *
 * @param <E> the object's type.
 * @author JavaSaBr
 */
public class ConcurrentStampedLockPool<E> extends ConcurrentPool<E> {

    public ConcurrentStampedLockPool(@NotNull Class<? super E> type) {
        super(type);
    }

    @Override
    protected @NotNull ConcurrentArray<E> createPool(@NotNull Class<? super E> type) {
        return ArrayFactory.newConcurrentStampedLockArray(type);
    }
}
