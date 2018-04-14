package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.concurrent.lock.AsyncReadSyncWriteLock;

/**
 * The concurrent implementation of the array using {@link LockFactory#newReentrantARSWLock()} for {@link
 * ConcurrentArray#readLock()}* and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockArray<E> extends AbstractConcurrentArray<E> {

    private static final long serialVersionUID = -6291504312637658721L;

    /**
     * The locker.
     */
    private final AsyncReadSyncWriteLock lock;

    /**
     * Instantiates a new Concurrent atomic arsw lock array.
     *
     * @param type the type
     */
    public ConcurrentAtomicARSWLockArray(@NotNull final Class<E> type) {
        this(type, 10);
    }

    /**
     * Instantiates a new Concurrent atomic arsw lock array.
     *
     * @param type the type
     * @param size the size
     */
    public ConcurrentAtomicARSWLockArray(@NotNull final Class<E> type, final int size) {
        super(type, size);

        this.lock = LockFactory.newAtomicARSWLock();
    }

    @Override
    public final long readLock() {
        lock.asyncLock();
        return 0;
    }

    @Override
    public void readUnlock(final long stamp) {
        lock.asyncUnlock();
    }

    @Override
    public final long writeLock() {
        lock.syncLock();
        return 0;
    }

    @Override
    public void writeUnlock(final long stamp) {
        lock.syncUnlock();
    }
}