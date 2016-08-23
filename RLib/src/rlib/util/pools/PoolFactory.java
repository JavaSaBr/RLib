package rlib.util.pools;

import rlib.util.pools.impl.ConcurrentPrimitiveAtomicARSWLockPool;
import rlib.util.pools.impl.ConcurrentPrimitiveAtomicARSWLockReusablePool;
import rlib.util.pools.impl.ConcurrentReentrantRWLockPool;
import rlib.util.pools.impl.ConcurrentStampedLockPool;
import rlib.util.pools.impl.FastPool;
import rlib.util.pools.impl.FastReusablePool;
import rlib.util.pools.impl.SynchronizedReusablePool;

/**
 * The factory for creating new pools.
 *
 * @author JavaSaBr
 */
public final class PoolFactory {

    public static <T extends Reusable> ReusablePool<T> newConcurrentPrimitiveAtomicARSWLockReusablePool(final Class<? extends Reusable> type) {
        return new ConcurrentPrimitiveAtomicARSWLockReusablePool<>(type);
    }

    public static <T extends Reusable> ReusablePool<T> newConcurrentStampedLockReusablePool(final Class<? extends Reusable> type) {
        return new ConcurrentStampedLockPool<>(type);
    }

    public static <T extends Reusable> ReusablePool<T> newConcurrentReentrantRWLockReusablePool(final Class<? extends Reusable> type) {
        return new ConcurrentReentrantRWLockPool<>(type);
    }

    public static <T extends Reusable> ReusablePool<T> newSynchronizedReusablePool(final Class<? extends Reusable> type) {
        return new SynchronizedReusablePool<>(type);
    }

    public static <T extends Reusable> ReusablePool<T> newReusablePool(final Class<? extends Reusable> type) {
        return new FastReusablePool<>(type);
    }

    public static <T> Pool<T> newPrimitiveAtomicARSWLockPool(final Class<?> type) {
        return new ConcurrentPrimitiveAtomicARSWLockPool<>(type);
    }

    public static <T> Pool<T> newPool(final Class<?> type) {
        return new FastPool<>(type);
    }

    private PoolFactory() {
        throw new IllegalArgumentException();
    }
}
