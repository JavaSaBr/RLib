package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.StampedLock;

/**
 * The concurrent implementation of array using {@link StampedLock} for {@link ConcurrentArray#readLock()} and
 * {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
public class ConcurrentStampedLockArray<E> extends AbstractConcurrentArray<E> {

    private static final long serialVersionUID = -6291504312637658721L;

    private final @NotNull StampedLock lock;

    public ConcurrentStampedLockArray(@NotNull Class<? super E> type) {
        this(type, 10);
    }

    public ConcurrentStampedLockArray(@NotNull Class<? super E> type, int size) {
        super(type, size);
        this.lock = LockFactory.newStampedLock();
    }

    @Override
    public final long readLock() {
        return lock.readLock();
    }

    @Override
    public final long tryOptimisticRead() {
        return lock.tryOptimisticRead();
    }

    @Override
    public final boolean validate(long stamp) {
        return lock.validate(stamp);
    }

    @Override
    public final void readUnlock(long stamp) {
        lock.unlockRead(stamp);
    }

    @Override
    public final long writeLock() {
        return lock.writeLock();
    }

    @Override
    public final void writeUnlock(long stamp) {
        lock.unlockWrite(stamp);
    }
}
