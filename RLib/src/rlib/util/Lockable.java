package rlib.util;

/**
 * The interface for implementing the methods lock()/unlock() for any object.
 *
 * @author JavaSaBr
 */
public interface Lockable {

    /**
     * Lock this object.
     */
    void lock();

    /**
     * Unlock this object.
     */
    void unlock();
}
