package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.StampedLock;

/**
 * The implementation of the {@link ConcurrentObjectDictionary} using {@link
 * LockFactory#newAtomicARSWLock()}*.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class ConcurrentStampedLockObjectDictionary<K, V> extends AbstractConcurrentObjectDictionary<K, V> {

    private final @NotNull StampedLock lock;

    public ConcurrentStampedLockObjectDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    public ConcurrentStampedLockObjectDictionary(float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    public ConcurrentStampedLockObjectDictionary(int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    public ConcurrentStampedLockObjectDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
        this.lock = new StampedLock();
    }

    @Override
    public long readLock() {
        return lock.readLock();
    }

    @Override
    public void readUnlock(long stamp) {
        lock.unlockRead(stamp);
    }

    @Override
    public void writeUnlock(long stamp) {
        lock.unlockWrite(stamp);
    }

    @Override
    public long writeLock() {
        return lock.writeLock();
    }
}
