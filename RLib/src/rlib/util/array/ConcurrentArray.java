package rlib.util.array;

/**
 * The interface with methods for supporting threadsafe for the Array.
 *
 * @author JavaSaBr.
 */
public interface ConcurrentArray<E> extends Array<E> {

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
     */
    default void readUnlock() {
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
    default boolean validate(final long stamp) {
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
     */
    default void writeUnlock() {
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
}
