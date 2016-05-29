package rlib.util.pools;

import rlib.util.pools.impl.AtomicPool;
import rlib.util.pools.impl.AtomicReusablePool;
import rlib.util.pools.impl.ConcurrentReusablePool;
import rlib.util.pools.impl.FastPool;
import rlib.util.pools.impl.FastReusablePool;
import rlib.util.pools.impl.SynchronizedReusablePool;

/**
 * Фабрика пулов.
 *
 * @author Ronn
 */
public final class PoolFactory {

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see AtomicReusablePool
     */
    @Deprecated
    public static <T extends Reusable> ReusablePool<T> newAtomicFoldablePool(final Class<? extends Reusable> type) {
        return new AtomicReusablePool<T>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see AtomicReusablePool
     */
    public static <T extends Reusable> ReusablePool<T> newAtomicReusablePool(final Class<? extends Reusable> type) {
        return new AtomicReusablePool<T>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see AtomicPool
     */
    public static <T> Pool<T> newAtomicPool(final Class<?> type) {
        return new AtomicPool<T>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see ConcurrentReusablePool
     */
    @Deprecated
    public static <T extends Reusable> ReusablePool<T> newConcurrentFoldablePool(final Class<? extends Reusable> type) {
        return new ConcurrentReusablePool<T>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see ConcurrentReusablePool
     */
    public static <T extends Reusable> ReusablePool<T> newConcurrentReusablePool(final Class<? extends Reusable> type) {
        return new ConcurrentReusablePool<T>(type);
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
        return new FastReusablePool<T>(type);
    }

    /**
     * Создание нового не потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see FastReusablePool
     */
    public static <T extends Reusable> ReusablePool<T> newReusablePool(final Class<? extends Reusable> type) {
        return new FastReusablePool<T>(type);
    }

    /**
     * Создание нового не потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see FastPool
     */
    public static <T> Pool<T> newPool(final Class<?> type) {
        return new FastPool<T>(type);
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
        return new SynchronizedReusablePool<T>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see SynchronizedReusablePool
     */
    public static <T extends Reusable> ReusablePool<T> newSynchronizedReusablePool(final Class<? extends Reusable> type) {
        return new SynchronizedReusablePool<T>(type);
    }

    private PoolFactory() {
        throw new IllegalArgumentException();
    }
}
