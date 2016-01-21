package rlib.util.pools.impl;

import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Foldable;
import rlib.util.pools.FoldablePool;

/**
 * Реализация потокобезопасного {@link FoldablePool} за счет синхронизации на
 * коллекции объектов.
 *
 * @author Ronn
 */
public class SynchronizedFoldablePool<E extends Foldable> implements FoldablePool<E> {

    /**
     * Пул объектов.
     */
    private final Array<E> pool;

    public SynchronizedFoldablePool(final Class<?> type) {
        this.pool = ArrayFactory.newArray(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(final E object) {

        if (object == null) {
            return;
        }

        object.finalyze();

        ArrayUtils.addInSynchronizeTo(pool, object);
    }

    @Override
    public void remove(final E object) {
        ArrayUtils.fastRemoveInSynchronizeTo(pool, object);
    }

    @Override
    public E take() {

        E object = null;

        synchronized (pool) {
            object = pool.pop();
        }

        if (object == null) {
            return null;
        }

        object.reinit();

        return object;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
