package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

/**
 * The concurrent implementation of the array without duplications using {@link LockFactory#newReentrantARSWLock()} for
 * {@link ConcurrentArray#readLock()} and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockArraySet<E> extends ConcurrentReentrantRWLockArray<E> {

    private static final long serialVersionUID = -3394386864246350866L;

    /**
     * Instantiates a new Concurrent atomic arsw lock array set.
     *
     * @param type the type
     */
    public ConcurrentAtomicARSWLockArraySet(final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Concurrent atomic arsw lock array set.
     *
     * @param type the type
     * @param size the size
     */
    public ConcurrentAtomicARSWLockArraySet(final Class<E> type, final int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull final E element) {
        return !contains(element) && super.add(element);
    }
}
