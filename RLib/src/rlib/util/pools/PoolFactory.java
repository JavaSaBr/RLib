package rlib.util.pools;

import org.jetbrains.annotations.NotNull;
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

    @NotNull
    public static <T extends Reusable> ReusablePool<T> newConcurrentAtomicARSWLockReusablePool(final Class<? extends Reusable> type) {
        return new FinalConcurrentAtomicARSWLockReusablePool<>(type);
    }

    @NotNull
    public static <T extends Reusable> ReusablePool<T> newConcurrentStampedLockReusablePool(final Class<? extends Reusable> type) {
        return new FinalConcurrentStampedLockReusablePool<>(type);
    }

    @NotNull
    public static <T extends Reusable> ReusablePool<T> newConcurrentReentrantRWLockReusablePool(final Class<? extends Reusable> type) {
        return new ConcurrentReentrantRWLockPool<>(type);
    }

    @NotNull
    public static <T extends Reusable> ReusablePool<T> newSynchronizedReusablePool(final Class<? extends Reusable> type) {
        return new SynchronizedReusablePool<>(type);
    }

    @NotNull
    public static <T extends Reusable> ReusablePool<T> newReusablePool(final Class<? extends Reusable> type) {
        return new FinalFastReusablePool<>(type);
    }

    @NotNull
    public static <T> Pool<T> newConcurrentAtomicARSWLockPool(final Class<?> type) {
        return new FinalConcurrentAtomicARSWLockPool<>(type);
    }

    @NotNull
    public static <T> Pool<T> newPool(final Class<?> type) {
        return new FinalFastPool<>(type);
    }

    private PoolFactory() {
        throw new IllegalArgumentException();
    }
}
