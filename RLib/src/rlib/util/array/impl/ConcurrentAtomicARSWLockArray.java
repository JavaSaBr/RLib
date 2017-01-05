package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;
import rlib.util.array.ConcurrentArray;

/**
 * The concurrent implementation of the array using {@link LockFactory#newReentrantARSWLock()} for {@link
 * ConcurrentArray#readLock()} and {@link ConcurrentArray#writeLock()}.
 *
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockArray<E> extends AbstractConcurrentArray<E> {

    private static final long serialVersionUID = -6291504312637658721L;

    /**
     * The locker.
     */
    private final AsyncReadSyncWriteLock lock;

    public ConcurrentAtomicARSWLockArray(@NotNull final Class<E> type) {
        this(type, 10);
    }

    public ConcurrentAtomicARSWLockArray(@NotNull final Class<E> type, final int size) {
        super(type, size);

        this.lock = LockFactory.newAtomicARSWLock();
    }

    @Override
    public final long readLock() {
        lock.asyncLock();
        return 0;
    }

    @Override
    public void readUnlock(final long stamp) {
        lock.asyncUnlock();
    }

    @Override
    public final long writeLock() {
        lock.syncLock();
        return 0;
    }

    @Override
    public void writeUnlock(final long stamp) {
        lock.syncUnlock();
    }
}