package com.ss.rlib.common.util;

/**
 * The interface for implementing the copy method of this object.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
public interface Copyable<T> {

    /**
     * Create a new copy of this object.
     *
     * @return the new copy of this object.
     */
    T copy();
}
