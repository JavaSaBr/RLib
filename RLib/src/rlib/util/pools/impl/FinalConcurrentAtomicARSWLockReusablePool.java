package rlib.util.pools.impl;

import rlib.util.pools.Reusable;

/**
 * The final version of the {@link ConcurrentAtomicARSWLockReusablePool}.
 *
 * @author JavaSaBr
 */
public class FinalConcurrentAtomicARSWLockReusablePool<E extends Reusable> extends ConcurrentAtomicARSWLockReusablePool<E> {

    public FinalConcurrentAtomicARSWLockReusablePool(final Class<?> type) {
        super(type);
    }
}
