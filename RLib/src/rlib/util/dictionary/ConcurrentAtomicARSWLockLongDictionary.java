package rlib.util.dictionary;

import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * The implementation of the {@link ConcurrentLongDictionary} using {@link LockFactory#newAtomicARSWLock()}.
 *
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockLongDictionary<V> extends AbstractConcurrentLongDictionary<V> {

    /**
     * The lock.
     */
    private final AsyncReadSyncWriteLock lock;

    public ConcurrentAtomicARSWLockLongDictionary() {
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockLongDictionary(final float loadFactor) {
        super(loadFactor);
        this.lock = LockFactory.newAtomicARSWLock();
    }

    public ConcurrentAtomicARSWLockLongDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
        this.lock = LockFactory.newAtomicARSWLock();
    }

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
