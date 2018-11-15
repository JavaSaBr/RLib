package com.ss.rlib.common.util.pools;

/**
 * The interface for implementing reusable objects. You can use reusable objects with {@link
 * ReusablePool}*.
 *
 * @author JavaSaBr
 */
public interface Reusable extends AutoCloseable {

    /**
     * Cleanup this object before storing to a pool.
     */
    default void free() {
    }

    /**
     * Prepares to reuse this object before taking from a pool.
     */
    default void reuse() {
    }

    /**
     * Stores this object to a pool.
     */
    default void release() {
    }

    @Override
    default void close() {
        release();
    }
}
