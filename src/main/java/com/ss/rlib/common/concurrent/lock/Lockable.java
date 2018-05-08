package com.ss.rlib.common.concurrent.lock;

/**
 * The interface to mark a object that it has a lock/unlock methods.
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
