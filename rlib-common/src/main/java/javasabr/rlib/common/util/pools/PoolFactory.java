package javasabr.rlib.common.util.pools;

import javasabr.rlib.common.concurrent.lock.impl.AtomicReadWriteLock;
import javasabr.rlib.common.concurrent.lock.impl.ReentrantARSWLock;
import javasabr.rlib.common.util.pools.impl.ConcurrentAtomicARSWLockPool;
import javasabr.rlib.common.util.pools.impl.ConcurrentAtomicARSWLockReusablePool;
import javasabr.rlib.common.util.pools.impl.ConcurrentReentrantRWLockPool;
import javasabr.rlib.common.util.pools.impl.ConcurrentStampedLockPool;
import javasabr.rlib.common.util.pools.impl.ConcurrentStampedLockReusablePool;
import javasabr.rlib.common.util.pools.impl.FastPool;
import javasabr.rlib.common.util.pools.impl.FastReusablePool;
import javasabr.rlib.common.util.pools.impl.SynchronizedReusablePool;
import org.jetbrains.annotations.NotNull;

/**
 * The factory for creating new pools.
 *
 * @author JavaSaBr
 */
public final class PoolFactory {

    /**
     * Create a reusable pool based on {@link AtomicReadWriteLock}.
     *
     * @param type the object's type.
     * @param <T>  the object's type.
     * @return the reusable pool.
     */
    public static <T extends Reusable> @NotNull ReusablePool<T> newConcurrentAtomicARSWLockReusablePool(
            @NotNull Class<? super T> type
    ) {
        return new ConcurrentAtomicARSWLockReusablePool<>(type);
    }

    /**
     * Create a reusable pool based on {@link java.util.concurrent.locks.StampedLock}.
     *
     * @param type the object's type.
     * @param <T>  the object's type.
     * @return the reusable pool.
     */
    public static <T extends Reusable> @NotNull ReusablePool<T> newConcurrentStampedLockReusablePool(
            @NotNull Class<? super T> type
    ) {
        return new ConcurrentStampedLockReusablePool<>(type);
    }

    /**
     * Create a reusable pool based on {@link ReentrantARSWLock}.
     *
     * @param type the object's type.
     * @param <T>  the object's type.
     * @return the reusable pool.
     */
    public static <T extends Reusable> @NotNull ReusablePool<T> newConcurrentReentrantRWLockReusablePool(
            @NotNull Class<? super T> type
    ) {
        return new ConcurrentReentrantRWLockPool<>(type);
    }

    /**
     * Create a reusable pool based on synchronization block.
     *
     * @param type the object's type.
     * @param <T>  the object's type.
     * @return the reusable pool.
     */
    public static <T extends Reusable> @NotNull ReusablePool<T> newSynchronizedReusablePool(
            @NotNull Class<? super T> type
    ) {
        return new SynchronizedReusablePool<>(type);
    }

    /**
     * Create a reusable pool.
     *
     * @param type the object's type.
     * @param <T>  the object's type.
     * @return the reusable pool.
     */
    public static <T extends Reusable> @NotNull ReusablePool<T> newReusablePool(@NotNull Class<? super T> type) {
        return new FastReusablePool<>(type);
    }

    /**
     * Create a pool based on {@link ReentrantARSWLock}.
     *
     * @param type the object's type.
     * @param <T>  the object's type.
     * @return the pool.
     */
    public static <T> @NotNull Pool<T> newConcurrentAtomicARSWLockPool(@NotNull Class<? super T> type) {
        return new ConcurrentAtomicARSWLockPool<>(type);
    }

    /**
     * Create a pool based on {@link java.util.concurrent.locks.StampedLock}.
     *
     * @param type the object's type.
     * @param <T>  the object's type.
     * @return the pool.
     */
    public static <T> @NotNull Pool<T> newConcurrentStampedLockPool(@NotNull Class<? super T> type) {
        return new ConcurrentStampedLockPool<>(type);
    }

    /**
     * Create a pool.
     *
     * @param type the object's type.
     * @param <T>  the object's type.
     * @return the reusable pool.
     */
    public static <T> @NotNull Pool<T> newPool(@NotNull Class<? super T> type) {
        return new FastPool<>(type);
    }

    private PoolFactory() {
        throw new IllegalArgumentException();
    }
}
