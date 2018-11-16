package com.ss.rlib.common.concurrent.lock.impl;

import com.ss.rlib.common.concurrent.lock.AsyncReadSyncWriteLock;
import com.ss.rlib.common.concurrent.lock.LockFactory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The wrapper of {@link ReentrantReadWriteLock} for implementing the interface {@link
 * AsyncReadSyncWriteLock}*.
 *
 * @author JavaSaBr
 */
public final class ReentrantARSWLock implements AsyncReadSyncWriteLock {

    /**
     * The locker of writing.
     */
    @NotNull
    private final Lock readLock;

    /**
     * The locker of reading.
     */
    @NotNull
    private final Lock writeLock;

    /**
     * Instantiates a new Reentrant arsw lock.
     */
    public ReentrantARSWLock() {
        final ReadWriteLock readWriteLock = LockFactory.newReentrantRWLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    @Override
    public void asyncLock() {
        readLock.lock();
    }

    @Override
    public void asyncUnlock() {
        readLock.unlock();
    }

    @Override
    public void syncLock() {
        writeLock.lock();
    }

    @Override
    public void syncUnlock() {
        writeLock.unlock();
    }
}
