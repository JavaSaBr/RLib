package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.concurrent.lock.AsyncReadSyncWriteLock;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of the {@link ConcurrentLongDictionary} using {@link
 * LockFactory#newAtomicARSWLock()}*.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockLongDictionary<V> extends AbstractConcurrentLongDictionary<V> {

    /**
     * The lock.
     */
    @NotNull
    private final AsyncReadSyncWriteLock lock;

    public ConcurrentAtomicARSWLockLongDictionary() {
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockLongDictionary(float loadFactor) {
        super(loadFactor);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockLongDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    /**
     * Instantiates a new Concurrent atomic arsw lock long dictionary.
     *
     * @param initCapacity the init capacity
     */
    public ConcurrentAtomicARSWLockLongDictionary(final int initCapacity) {
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
