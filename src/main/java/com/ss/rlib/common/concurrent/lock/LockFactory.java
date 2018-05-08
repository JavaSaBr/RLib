package com.ss.rlib.common.concurrent.lock;

import com.ss.rlib.common.concurrent.lock.impl.*;
import com.ss.rlib.common.concurrent.lock.impl.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * The factory for creating new locks.
 *
 * @author JavaSaBr
 */
public class LockFactory {

    /**
     * Create a new {@link ReentrantARSWLock}.
     *
     * @return the new lock.
     */
    public static AsyncReadSyncWriteLock newReentrantARSWLock() {
        return new ReentrantARSWLock();
    }

    /**
     * Create a new {@link FinalStampedLock}.
     *
     * @return the new lock.
     */
    public static StampedLock newStampedLock() {
        return new FinalStampedLock();
    }

    /**
     * Create a new {@link FinalReentrantLock}.
     *
     * @return the new lock.
     */
    public static Lock newReentrantLock() {
        return new FinalReentrantLock();
    }

    /**
     * Create a new {@link FinalAtomicReadWriteLock}.
     *
     * @return the new lock.
     */
    public static AsyncReadSyncWriteLock newAtomicARSWLock() {
        return new FinalAtomicReadWriteLock();
    }

    /**
     * Create a new {@link FinalAtomicLock}.
     *
     * @return the new lock.
     */
    public static Lock newAtomicLock() {
        return new FinalAtomicLock();
    }

    /**
     * Create a new {@link FinalReentrantReadWriteLock}.
     *
     * @return the new lock.
     */
    public static ReadWriteLock newReentrantRWLock() {
        return new FinalReentrantReadWriteLock();
    }

    /**
     * Create a new {@link FinalReentrantAtomicLock}.
     *
     * @return the new lock.
     */
    public static Lock newReentrantAtomicLock() {
        return new FinalReentrantAtomicLock();
    }
}
