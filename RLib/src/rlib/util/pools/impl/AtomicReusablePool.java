package rlib.util.pools.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Reusable;
import rlib.util.pools.ReusablePool;

import static rlib.util.ArrayUtils.getInWriteLock;
import static rlib.util.ArrayUtils.runInWriteLock;

/**
 * Реализация потокобезопасного {@link ReusablePool} с помощью атомарного блокировщика.
 *
 * @author JavaSaBr
 */
public class AtomicReusablePool<E extends Reusable> implements ReusablePool<E> {

    /**
     * Пул объектов.
     */
    private final Array<E> pool;

    public AtomicReusablePool(final Class<?> type) {
        this.pool = ArrayFactory.newConcurrentAtomicArray(type);
    }

    /**
     * @return контейнер объектов.
     */
    private Array<E> getPool() {
        return pool;
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(final E object) {
        if (object == null) return;
        object.free();
        runInWriteLock(pool, object, Array::add);
    }

    @Override
    public void remove(final E object) {
        runInWriteLock(pool, object, Array::fastRemove);
    }

    @Override
    public E take() {

        final Array<E> pool = getPool();
        if (pool.isEmpty()) return null;

        final E object = getInWriteLock(pool, Array::pop);
        if (object == null) return null;

        object.reuse();

        return object;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
