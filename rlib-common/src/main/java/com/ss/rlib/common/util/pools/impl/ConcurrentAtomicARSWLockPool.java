package com.ss.rlib.common.util.pools.impl;

import com.ss.rlib.common.util.array.impl.ConcurrentAtomicARSWLockArray;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.util.pools.Pool;

/**
 * The threadsafe implementation of the {@link Pool} using like a storage the {@link
 * ConcurrentAtomicARSWLockArray}*.
 *
 * @param <E> the object's type.
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockPool<E> extends ConcurrentPool<E> {

    public ConcurrentAtomicARSWLockPool(@NotNull Class<? super E> type) {
        super(type);
    }

    @Override
    protected @NotNull ConcurrentArray<E> createPool(@NotNull Class<? super E> type) {
        return ArrayFactory.newConcurrentAtomicARSWLockArray(type);
    }
}
