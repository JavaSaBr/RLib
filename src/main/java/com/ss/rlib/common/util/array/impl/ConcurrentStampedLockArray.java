package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.StampedLock;

/**
 * The concurrent implementation of the array using {@link StampedLock} for {@link ConcurrentArray#readLock()} and
 * {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentStampedLockArray<E> extends AbstractConcurrentArray<E> {

    private static final long serialVersionUID = -6291504312637658721L;

    /**
     * The locker.
     */
    private final StampedLock lock;

    /**
     * Instantiates a new Concurrent stamped lock array.
     *
     * @param type the type
     */
    public ConcurrentStampedLockArray(@NotNull final Class<E> type) {
        this(type, 10);
    }

    /**
     * Instantiates a new Concurrent stamped lock array.
     *
     * @param type the type
     * @param size the size
     */
    public ConcurrentStampedLockArray(@NotNull final Class<E> type, final int size) {
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