package com.ss.rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.ss.rlib.util.array.ConcurrentArray;

/**
 * The concurrent implementation of the array without duplications using {@link ReentrantReadWriteLock} for {@link
 * ConcurrentArray#readLock()}* and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentReentrantRWLockArraySet<E> extends ConcurrentReentrantRWLockArray<E> {

    private static final long serialVersionUID = -3394386864246350866L;

    /**
     * Instantiates a new Concurrent reentrant rw lock array set.
     *
     * @param type the type
     */
    public ConcurrentReentrantRWLockArraySet(final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Concurrent reentrant rw lock array set.
     *
     * @param type the type
     * @param size the size
     */
    public ConcurrentReentrantRWLockArraySet(final Class<E> type, final int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull final E element) {
        return !contains(element) && super.add(element);
    }
}
