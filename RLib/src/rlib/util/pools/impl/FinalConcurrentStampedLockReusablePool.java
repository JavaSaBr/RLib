package rlib.util.pools.impl;

import rlib.util.pools.Reusable;

/**
 * The final version of the {@link ConcurrentStampedLockReusablePool}.
 *
 * @author JavaSaBr
 */
public class FinalConcurrentStampedLockReusablePool<E extends Reusable> extends ConcurrentStampedLockReusablePool<E> {

    public FinalConcurrentStampedLockReusablePool(final Class<?> type) {
        super(type);
    }
}
