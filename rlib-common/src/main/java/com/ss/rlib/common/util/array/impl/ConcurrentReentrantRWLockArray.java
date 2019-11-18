package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The concurrent implementation of the array using {@link ReentrantReadWriteLock} for {@link
 * ConcurrentArray#readLock()}* and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
public class ConcurrentReentrantRWLockArray<E> extends AbstractConcurrentArray<E> {

    private static final long serialVersionUID = -7985171224116955303L;

    private final @NotNull Lock readLock;
    private final @NotNull Lock writeLock;

    public ConcurrentReentrantRWLockArray(@NotNull Class<? super E> type) {
        this(type, 10);
    }

    public ConcurrentReentrantRWLockArray(@NotNull Class<? super E> type, int size) {
        super(type, size);
        var readWriteLock = LockFactory.newReentrantRWLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }

    @Override
    public final long readLock() {
        readLock.lock();
        return 1;
    }

    @Override
    public final void readUnlock(long stamp) {
        readLock.unlock();
    }

    @Override
    public final long writeLock() {
        writeLock.lock();
        return 1;
    }

    @Override
    public final void writeUnlock(long stamp) {
        writeLock.unlock();
    }
}
