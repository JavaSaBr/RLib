package com.ss.rlib.util.pools.impl;

/**
 * The final version of the {@link ConcurrentAtomicARSWLockPool}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FinalConcurrentAtomicARSWLockPool<E> extends ConcurrentAtomicARSWLockPool<E> {

    /**
     * Instantiates a new Final concurrent atomic arsw lock pool.
     *
     * @param type the type
     */
    public FinalConcurrentAtomicARSWLockPool(final Class<?> type) {
        super(type);
    }
}
