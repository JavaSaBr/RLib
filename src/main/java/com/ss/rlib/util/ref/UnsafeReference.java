package com.ss.rlib.util.ref;

/**
 * The interface for the {@link Reference} with unsafe methods.
 *
 * @author JavaSaBr
 */
public interface UnsafeReference extends Reference {

    /**
     * Is thread local boolean.
     *
     * @return the boolean
     */
    boolean isThreadLocal();
}
