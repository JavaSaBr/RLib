package com.ss.rlib.common.concurrent.atomic;

import com.ss.rlib.common.util.pools.Reusable;
import com.ss.rlib.common.util.pools.Reusable;

/**
 * The atomic reference with additional methods.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public final class AtomicReference<V> extends java.util.concurrent.atomic.AtomicReference<V> implements Reusable {

    private static final long serialVersionUID = -4058945159519762615L;

    /**
     * Instantiates a new Atomic reference.
     */
    public AtomicReference() {
    }

    /**
     * Instantiates a new Atomic reference.
     *
     * @param initialValue the initial value
     */
    public AtomicReference(final V initialValue) {
        super(initialValue);
    }
}
