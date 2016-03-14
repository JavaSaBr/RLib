package rlib.concurrent.lock.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация обертки {@link ReentrantReadWriteLock} для реализации интерфейса {@link
 * AsyncReadSyncWriteLock}.
 *
 * @author Ronn
 */
public final class SimpleReadWriteLock implements AsyncReadSyncWriteLock {

    /**
     * Блокировщик записи.
     */
    private final Lock readLock;

    /**
     * Блокировщик чтения.
     */
    private final Lock writeLock;

    public SimpleReadWriteLock() {
        final ReadWriteLock readWriteLock = LockFactory.newRWLock();
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
