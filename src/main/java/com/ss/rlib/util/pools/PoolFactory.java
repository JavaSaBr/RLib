package com.ss.rlib.util.pools;

import com.ss.rlib.util.pools.impl.ConcurrentReentrantRWLockPool;
import com.ss.rlib.util.pools.impl.FinalConcurrentAtomicARSWLockPool;
import com.ss.rlib.util.pools.impl.SynchronizedReusablePool;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.util.pools.impl.FinalConcurrentAtomicARSWLockReusablePool;
import com.ss.rlib.util.pools.impl.FinalConcurrentStampedLockReusablePool;
import com.ss.rlib.util.pools.impl.FinalFastPool;
import com.ss.rlib.util.pools.impl.FinalFastReusablePool;

/**
 * The factory for creating new pools.
 *
 * @author JavaSaBr
 */
public final class PoolFactory {

    /**
     * New concurrent atomic arsw lock reusable pool reusable pool.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the reusable pool
     */
    @NotNull
    public static <T extends Reusable> ReusablePool<T> newConcurrentAtomicARSWLockReusablePool(final Class<? extends Reusable> type) {
        return new FinalConcurrentAtomicARSWLockReusablePool<>(type);
    }

    /**
     * New concurrent stamped lock reusable pool reusable pool.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the reusable pool
     */
    @NotNull
    public static <T extends Reusable> ReusablePool<T> newConcurrentStampedLockReusablePool(final Class<? extends Reusable> type) {
        return new FinalConcurrentStampedLockReusablePool<>(type);
    }

    /**
     * New concurrent reentrant rw lock reusable pool reusable pool.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the reusable pool
     */
    @NotNull
    public static <T extends Reusable> ReusablePool<T> newConcurrentReentrantRWLockReusablePool(final Class<? extends Reusable> type) {
        return new ConcurrentReentrantRWLockPool<>(type);
    }

    /**
     * New synchronized reusable pool reusable pool.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the reusable pool
     */
    @NotNull
    public static <T extends Reusable> ReusablePool<T> newSynchronizedReusablePool(final Class<? extends Reusable> type) {
        return new SynchronizedReusablePool<>(type);
    }

    /**
     * New reusable pool reusable pool.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the reusable pool
     */
    @NotNull
    public static <T extends Reusable> ReusablePool<T> newReusablePool(final Class<? extends Reusable> type) {
        return new FinalFastReusablePool<>(type);
    }

    /**
     * New concurrent atomic arsw lock pool pool.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the pool
     */
    @NotNull
    public static <T> Pool<T> newConcurrentAtomicARSWLockPool(final Class<?> type) {
        return new FinalConcurrentAtomicARSWLockPool<>(type);
    }

    /**
     * New pool pool.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the pool
     */
    @NotNull
    public static <T> Pool<T> newPool(final Class<?> type) {
        return new FinalFastPool<>(type);
    }

    private PoolFactory() {
        throw new IllegalArgumentException();
    }
}
