package com.ss.rlib.common.util.linkedlist;

/**
 * The interface to implement concurrent supporting.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public interface ConcurrentLinkedList<E> extends LinkedList<E> {

    /**
     * Lock this array for writing.
     *
     * @return the stamp of write lock or 0.
     */
    default long writeLock() {
        throw new UnsupportedOperationException();
    }

    /**
     * Unlock the write lock.
     *
     * @param stamp the stamp of write lock.
     */
    default void writeUnlock(final long stamp) {
        throw new UnsupportedOperationException();
    }

    /**
     * Lock this array for reading.
     *
     * @return the stamp of read lock or 0.
     */
    default long readLock() {
        throw new UnsupportedOperationException();
    }

    /**
     * Unlock the read lock.
     *
     * @param stamp the stamp of read lock.
     */
    default void readUnlock(final long stamp) {
        throw new UnsupportedOperationException();
    }
}
