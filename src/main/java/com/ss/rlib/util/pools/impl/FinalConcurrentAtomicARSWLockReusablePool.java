package com.ss.rlib.util.pools.impl;

import com.ss.rlib.util.pools.Reusable;

/**
 * The final version of the {@link ConcurrentAtomicARSWLockReusablePool}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FinalConcurrentAtomicARSWLockReusablePool<E extends Reusable> extends ConcurrentAtomicARSWLockReusablePool<E> {

    /**
     * Instantiates a new Final concurrent atomic arsw lock reusable pool.
     *
     * @param type the type
     */
    public FinalConcurrentAtomicARSWLockReusablePool(final Class<?> type) {
        super(type);
    }
}
