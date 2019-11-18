package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

import com.ss.rlib.common.concurrent.lock.AsyncReadSyncWriteLock;

/**
 * The concurrent implementation of the array using {@link LockFactory#newReentrantARSWLock()} for {@link
 * ConcurrentArray#readLock()}* and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockArray<E> extends AbstractConcurrentArray<E> {

    private static final long serialVersionUID = -6291504312637658721L;

    private final @NotNull AsyncReadSyncWriteLock lock;

    public ConcurrentAtomicARSWLockArray(@NotNull Class<? super E> type) {
        this(type, 10);
    }

    public ConcurrentAtomicARSWLockArray(@NotNull Class<? super E> type, int size) {
        super(type, size);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    @Override
    public final long readLock() {
        lock.asyncLock();
        return 1;
    }

    @Override
    public void readUnlock(long stamp) {
        lock.asyncUnlock();
    }

    @Override
    public final long writeLock() {
        lock.syncLock();
        return 1;
    }

    @Override
    public void writeUnlock(long stamp) {
        lock.syncUnlock();
    }
}
