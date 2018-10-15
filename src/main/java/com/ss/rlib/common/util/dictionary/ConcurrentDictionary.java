package com.ss.rlib.common.util.dictionary;

/**
 * The interface with methods for supporting threadsafe for the {@link Dictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public interface ConcurrentDictionary<K, V> extends Dictionary<K, V> {

    /**
     * Lock this dictionary for reading.
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
    default void readUnlock(long stamp) {
        throw new UnsupportedOperationException();
    }

    /**
     * Try to optimistic read.
     *
     * @return the stamp of optimistic read or 0 if it was failed.
     */
    default long tryOptimisticRead() {
        throw new UnsupportedOperationException();
    }

    /**
     * Validate this stamp.
     *
     * @param stamp the stamp.
     * @return true is this stamp is valid.
     */
    default boolean validate(long stamp) {
        throw new UnsupportedOperationException();
    }

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
    default void writeUnlock(long stamp) {
        throw new UnsupportedOperationException();
    }
}
