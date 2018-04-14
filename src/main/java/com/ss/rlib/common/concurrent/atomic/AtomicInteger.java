package com.ss.rlib.common.concurrent.atomic;

import com.ss.rlib.common.util.pools.Reusable;
import com.ss.rlib.common.util.pools.Reusable;

/**
 * The atomic integer with additional methods.
 *
 * @author JavaSaBr
 */
public final class AtomicInteger extends java.util.concurrent.atomic.AtomicInteger implements Reusable {

    private static final long serialVersionUID = -624766818867950719L;

    /**
     * Instantiates a new Atomic integer.
     */
    public AtomicInteger() {
    }

    /**
     * Instantiates a new Atomic integer.
     *
     * @param initialValue the initial value
     */
    public AtomicInteger(final int initialValue) {
        super(initialValue);
    }

    /**
     * Atomically decrements by delta the current value.
     *
     * @param delta the delta.
     * @return the result value.
     */
    public final int subAndGet(final int delta) {

        int current;
        int next;

        do {
            current = get();
            next = current - delta;
        }
        while (!compareAndSet(current, next));

        return next;
    }
}
