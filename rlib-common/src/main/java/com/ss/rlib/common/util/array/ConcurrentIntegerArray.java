package com.ss.rlib.common.util.array;

public interface ConcurrentIntegerArray {

    /**
     * Write lock.
     */
    default void writeLock() {
    }

    /**
     * Write unlock.
     */
    default void writeUnlock() {
    }

    /**
     * Read lock.
     */
    default void readLock() {
    }

    /**
     * Read unlock.
     */
    default void readUnlock() {
    }
}
