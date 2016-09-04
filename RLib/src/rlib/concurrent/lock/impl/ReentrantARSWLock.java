package rlib.concurrent.lock.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * The wrapper of {@link ReentrantReadWriteLock} for implementing the interface {@link
 * AsyncReadSyncWriteLock}.
 *
 * @author JavaSaBr
 */
public final class ReentrantARSWLock implements AsyncReadSyncWriteLock {

    /**
     * The locker of writing.
     */
    private final Lock readLock;

    /**
     * The locker of reading.
     */
    private final Lock writeLock;

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
