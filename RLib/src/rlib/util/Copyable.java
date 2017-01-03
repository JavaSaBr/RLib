package rlib.util;

/**
 * The interface for implementing the copy method of this object.
 *
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
