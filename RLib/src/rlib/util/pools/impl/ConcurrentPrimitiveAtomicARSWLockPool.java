package rlib.util.pools.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.array.ConcurrentArray;
import rlib.util.array.impl.ConcurrentPrimitiveAtomicARSWLockArray;
import rlib.util.pools.Pool;

import static rlib.util.ArrayUtils.getInWriteLock;
import static rlib.util.ArrayUtils.runInWriteLock;

/**
 * The threadsafe implementation of the {@link Pool} using like a storage the {@link
 * ConcurrentPrimitiveAtomicARSWLockArray}.
 *
 * @author JavaSaBr
 */
public class ConcurrentPrimitiveAtomicARSWLockPool<E> implements Pool<E> {

    /**
     * The storage of objects.
     */
    private final ConcurrentArray<E> pool;

    public ConcurrentPrimitiveAtomicARSWLockPool(final Class<?> type) {
        this.pool = ArrayFactory.newConcurrentPrimitiveAtomicARSWLockArray(type);
    }

    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }

    @Override
    public void put(@NotNull final E object) {
        runInWriteLock(pool, object, Array::add);
    }

    @Override
    public void remove(@NotNull final E object) {
        runInWriteLock(pool, object, Array::fastRemove);
    }

    @Nullable
    @Override
    public E take() {

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
