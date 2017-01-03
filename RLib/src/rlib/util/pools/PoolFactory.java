package rlib.util.pools;

import rlib.util.pools.impl.ConcurrentReentrantRWLockPool;
import rlib.util.pools.impl.FinalConcurrentAtomicARSWLockPool;
import rlib.util.pools.impl.FinalConcurrentAtomicARSWLockReusablePool;
import rlib.util.pools.impl.FinalConcurrentStampedLockReusablePool;
import rlib.util.pools.impl.FinalFastPool;
import rlib.util.pools.impl.FinalFastReusablePool;
import rlib.util.pools.impl.SynchronizedReusablePool;

/**
 * The factory for creating new pools.
 *
 * @author JavaSaBr
 */
public final class PoolFactory {

    public static <T extends Reusable> ReusablePool<T> newConcurrentAtomicARSWLockReusablePool(final Class<? extends Reusable> type) {
        return new FinalConcurrentAtomicARSWLockReusablePool<>(type);
    }

    public static <T extends Reusable> ReusablePool<T> newConcurrentStampedLockReusablePool(final Class<? extends Reusable> type) {
        return new FinalConcurrentStampedLockReusablePool<>(type);
    }

    public static <T extends Reusable> ReusablePool<T> newConcurrentReentrantRWLockReusablePool(final Class<? extends Reusable> type) {
        return new ConcurrentReentrantRWLockPool<>(type);
    }

    public static <T extends Reusable> ReusablePool<T> newSynchronizedReusablePool(final Class<? extends Reusable> type) {
        return new SynchronizedReusablePool<>(type);
    }

    public static <T extends Reusable> ReusablePool<T> newReusablePool(final Class<? extends Reusable> type) {
        return new FinalFastReusablePool<>(type);
    }

    public static <T> Pool<T> newConcurrentAtomicARSWLockPool(final Class<?> type) {
        return new FinalConcurrentAtomicARSWLockPool<>(type);
    }

    public static <T> Pool<T> newPool(final Class<?> type) {
        return new FinalFastPool<>(type);
    }

    private PoolFactory() {
        throw new IllegalArgumentException();
    }
}
