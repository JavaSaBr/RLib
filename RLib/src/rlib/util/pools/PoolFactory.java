package rlib.util.pools;

import rlib.util.pools.impl.ConcurrentPrimitiveAtomicARSWLockPool;
import rlib.util.pools.impl.ConcurrentReentrantRWLockPool;
import rlib.util.pools.impl.FastPool;
import rlib.util.pools.impl.FastReusablePool;
import rlib.util.pools.impl.PrimitiveAtomicARSWLockReusablePool;
import rlib.util.pools.impl.SynchronizedReusablePool;

/**
 * Фабрика пулов.
 *
 * @author JavaSaBr
 */
public final class PoolFactory {

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see PrimitiveAtomicARSWLockReusablePool
     */
    public static <T extends Reusable> ReusablePool<T> newAtomicReusablePool(final Class<? extends Reusable> type) {
        return new PrimitiveAtomicARSWLockReusablePool<>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see ConcurrentPrimitiveAtomicARSWLockPool
     */
    public static <T> Pool<T> newAtomicPool(final Class<?> type) {
        return new ConcurrentPrimitiveAtomicARSWLockPool<>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see ConcurrentReentrantRWLockPool
     */
    public static <T extends Reusable> ReusablePool<T> newConcurrentReusablePool(final Class<? extends Reusable> type) {
        return new ConcurrentReentrantRWLockPool<>(type);
    }

    /**
     * Создание нового не потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see FastReusablePool
     */
    @Deprecated
    public static <T extends Reusable> ReusablePool<T> newFoldablePool(final Class<? extends Reusable> type) {
        return new FastReusablePool<>(type);
    }

    /**
     * Создание нового не потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see FastReusablePool
     */
    public static <T extends Reusable> ReusablePool<T> newReusablePool(final Class<? extends Reusable> type) {
        return new FastReusablePool<>(type);
    }

    /**
     * Создание нового не потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see FastPool
     */
    public static <T> Pool<T> newPool(final Class<?> type) {
        return new FastPool<>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see SynchronizedReusablePool
     */
    @Deprecated
    public static <T extends Reusable> ReusablePool<T> newSynchronizedFoldablePool(final Class<? extends Reusable> type) {
        return new SynchronizedReusablePool<>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see SynchronizedReusablePool
     */
    public static <T extends Reusable> ReusablePool<T> newSynchronizedReusablePool(final Class<? extends Reusable> type) {
        return new SynchronizedReusablePool<>(type);
    }

    private PoolFactory() {
        throw new IllegalArgumentException();
    }
}
