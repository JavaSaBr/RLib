package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.concurrent.lock.AsyncReadSyncWriteLock;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    private final AsyncReadSyncWriteLock lock;

    public ConcurrentAtomicARSWLockObjectDictionary() {
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockObjectDictionary(float loadFactor) {
        super(loadFactor);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockObjectDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockObjectDictionary(int initCapacity) {
        super(initCapacity);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    @Override
    public long readLock() {
        lock.asyncLock();
        return 0;
    }

    @Override
    public void readUnlock(long stamp) {
        lock.asyncUnlock();
    }

    @Override
    public void writeUnlock(long stamp) {
        lock.syncUnlock();
    }

    @Override
    public long writeLock() {
        lock.syncLock();
        return 0;
    }
}
