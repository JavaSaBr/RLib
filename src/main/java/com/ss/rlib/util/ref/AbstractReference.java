package com.ss.rlib.util.ref;

/**
 * The base implementation of the {@link Reference}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractReference implements UnsafeReference {

    /**
     * Instantiates a new Abstract reference.
     */
    protected AbstractReference() {
        super();
    }

    @Override
    public boolean isThreadLocal() {
        return false;
    }

    @Override
    public void release() {
        getType().put(this);
    }
}
