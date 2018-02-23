package com.ss.rlib.util.dictionary;

import com.ss.rlib.concurrent.lock.LockFactory;
import com.ss.rlib.concurrent.lock.AsyncReadSyncWriteLock;

/**
 * The implementation of the {@link ConcurrentObjectDictionary} using {@link
 * LockFactory#newAtomicARSWLock()}*.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockObjectDictionary<K, V> extends AbstractConcurrentObjectDictionary<K, V> {

    /**
     * The lock.
     */
    private final AsyncReadSyncWriteLock lock;

    public ConcurrentAtomicARSWLockObjectDictionary() {
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockObjectDictionary(final float loadFactor) {
        super(loadFactor);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockObjectDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockObjectDictionary(final int initCapacity) {
        super(initCapacity);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    @Override
    public long readLock() {
        lock.asyncLock();
        return 0;
    }

    @Override
    public void readUnlock(final long stamp) {
        lock.asyncUnlock();
    }

    @Override
    public void writeUnlock(final long stamp) {
        lock.syncUnlock();
    }

    @Override
    public long writeLock() {
        lock.syncLock();
        return 0;
    }
}
