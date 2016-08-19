package rlib.util.pools.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Pool;

import static rlib.util.ArrayUtils.getInWriteLock;
import static rlib.util.ArrayUtils.runInWriteLock;

/**
 * Реализация потокобезопасного {@link Pool} с помощью атомарного блокировщика.
 *
 * @author JavaSaBr
 */
public class AtomicPool<E> implements Pool<E> {

    /**
     * Пул объектов.
     */
    private final Array<E> pool;

    public AtomicPool(final Class<?> type) {
        this.pool = ArrayFactory.newConcurrentAtomicArray(type);
    }

    /**
     * @return пул объектов.
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

        return object;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
