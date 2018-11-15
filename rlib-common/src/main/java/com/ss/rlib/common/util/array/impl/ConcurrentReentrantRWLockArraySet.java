package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The concurrent implementation of the array without duplications using {@link ReentrantReadWriteLock} for {@link
 * ConcurrentArray#readLock()}* and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentReentrantRWLockArraySet<E> extends ConcurrentReentrantRWLockArray<E> {

    private static final long serialVersionUID = -3394386864246350866L;

    public ConcurrentReentrantRWLockArraySet(Class<? super E> type) {
        super(type);
    }

    public ConcurrentReentrantRWLockArraySet(Class<? super E> type, int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull final E element) {
        return !contains(element) && super.add(element);
    }
}
