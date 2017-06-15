package com.ss.rlib.util.dictionary;

import com.ss.rlib.concurrent.lock.LockFactory;
import com.ss.rlib.concurrent.lock.AsyncReadSyncWriteLock;

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
    private final AsyncReadSyncWriteLock lock;

    /**
     * Instantiates a new Concurrent atomic arsw lock integer dictionary.
     */
    public ConcurrentAtomicARSWLockIntegerDictionary() {
        this.lock = LockFactory.newAtomicARSWLock();
    }

    /**
     * Instantiates a new Concurrent atomic arsw lock integer dictionary.
     *
     * @param loadFactor the load factor
     */
    public ConcurrentAtomicARSWLockIntegerDictionary(final float loadFactor) {
        super(loadFactor);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    /**
     * Instantiates a new Concurrent atomic arsw lock integer dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    public ConcurrentAtomicARSWLockIntegerDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    /**
     * Instantiates a new Concurrent atomic arsw lock integer dictionary.
     *
     * @param initCapacity the init capacity
     */
    public ConcurrentAtomicARSWLockIntegerDictionary(final int initCapacity) {
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
