package rlib.util.array.impl;

import java.util.concurrent.locks.StampedLock;

import rlib.concurrent.lock.LockFactory;
import rlib.util.array.ConcurrentArray;

/**
 * The concurrent implementation of the array using {@link StampedLock} for {@link
 * ConcurrentArray#readLock()} and {@link ConcurrentArray#writeLock()}.
 *
 * @author JavaSaBr
 */
public class ConcurrentStampedLockArray<E> extends AbstractConcurrentArray<E> {

    private static final long serialVersionUID = -6291504312637658721L;

    /**
     * The locker.
     */
    private final StampedLock lock;

    public ConcurrentStampedLockArray(final Class<E> type) {
        this(type, 10);
    }

    public ConcurrentStampedLockArray(final Class<E> type, final int size) {
        super(type, size);

        this.lock = LockFactory.newStampedLock();
    }

    @Override
    public final long readLock() {
        return lock.readLock();
    }

    @Override
    public long tryOptimisticRead() {
        return lock.tryOptimisticRead();
    }

    @Override
    public boolean validate(long stamp) {
        return lock.validate(stamp);
    }

    @Override
    public void readUnlock(final long stamp) {
        lock.unlockRead(stamp);
    }

    @Override
    public final long writeLock() {
        return lock.writeLock();
    }

    @Override
    public void writeUnlock(final long stamp) {
        lock.unlockWrite(stamp);
    }
}