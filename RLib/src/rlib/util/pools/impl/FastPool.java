package rlib.util.pools.impl;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.pools.Pool;

/**
 * Реализация не потокобезопасного легковесного {@link Pool}.
 *
 * @author JavaSaBr
 */
public class FastPool<E> implements Pool<E> {

    /**
     * Пул объектов.
     */
    private final Array<E> pool;

    public FastPool(final Class<?> type) {
        this.pool = ArrayFactory.newArray(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(final E object) {
        if (object == null) return;
        pool.add(object);
    }

    @Override
    public void remove(final E object) {
        pool.fastRemove(object);
    }

    @Override
    public E take() {
        final E object = pool.pop();
        if (object == null) return null;
        return object;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
