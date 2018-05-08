package com.ss.rlib.common.util.pools.impl;

import com.ss.rlib.common.util.pools.Reusable;

/**
 * The final version of the {@link ConcurrentStampedLockReusablePool}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FinalConcurrentStampedLockReusablePool<E extends Reusable> extends ConcurrentStampedLockReusablePool<E> {

    /**
     * Instantiates a new Final concurrent stamped lock reusable pool.
     *
     * @param type the type
     */
    public FinalConcurrentStampedLockReusablePool(final Class<?> type) {
        super(type);
    }
}
