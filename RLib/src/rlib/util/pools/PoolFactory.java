package rlib.util.pools;

import rlib.util.pools.impl.AtomicFoldablePool;
import rlib.util.pools.impl.AtomicPool;
import rlib.util.pools.impl.ConcurrentFoldablePool;
import rlib.util.pools.impl.FastFoldablePool;
import rlib.util.pools.impl.FastPool;
import rlib.util.pools.impl.SynchronizedFoldablePool;

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
     * @see AtomicFoldablePool
     */
    public static final <T extends Foldable> FoldablePool<T> newAtomicFoldablePool(final Class<? extends Foldable> type) {
        return new AtomicFoldablePool<T>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see AtomicPool
     */
    public static final <T> Pool<T> newAtomicPool(final Class<?> type) {
        return new AtomicPool<T>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see ConcurrentFoldablePool
     */
    public static final <T extends Foldable> FoldablePool<T> newConcurrentFoldablePool(final Class<? extends Foldable> type) {
        return new ConcurrentFoldablePool<T>(type);
    }

    /**
     * Создание нового не потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see FastFoldablePool
     */
    public static final <T extends Foldable> FoldablePool<T> newFoldablePool(final Class<? extends Foldable> type) {
        return new FastFoldablePool<T>(type);
    }

    /**
     * Создание нового не потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see FastPool
     */
    public static final <T> Pool<T> newPool(final Class<?> type) {
        return new FastPool<T>(type);
    }

    /**
     * Создание нового потокобезопасного объектного пула.
     *
     * @param type тип объектов пула.
     * @return новый объектный пул.
     * @see SynchronizedFoldablePool
     */
    public static final <T extends Foldable> FoldablePool<T> newSynchronizedFoldablePool(final Class<? extends Foldable> type) {
        return new SynchronizedFoldablePool<T>(type);
    }

    private PoolFactory() {
        throw new IllegalArgumentException();
    }
}
