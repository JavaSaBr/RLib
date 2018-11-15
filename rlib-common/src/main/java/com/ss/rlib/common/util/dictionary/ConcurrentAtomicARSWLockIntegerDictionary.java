package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.concurrent.lock.AsyncReadSyncWriteLock;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of the {@link ConcurrentIntegerDictionary} using {@link
 * LockFactory#newAtomicARSWLock()}*.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockIntegerDictionary<V> extends AbstractConcurrentIntegerDictionary<V> {

    /**
     * The lock.
     */
    @NotNull
    private final AsyncReadSyncWriteLock lock;

    public ConcurrentAtomicARSWLockIntegerDictionary() {
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockIntegerDictionary(float loadFactor) {
        super(loadFactor);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockIntegerDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockIntegerDictionary(int initCapacity) {
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
