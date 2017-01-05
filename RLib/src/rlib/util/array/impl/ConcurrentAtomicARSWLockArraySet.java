package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

import rlib.concurrent.lock.LockFactory;
import rlib.util.array.ConcurrentArray;

/**
 * The concurrent implementation of the array without duplications using {@link LockFactory#newReentrantARSWLock()} for
 * {@link ConcurrentArray#readLock()} and {@link ConcurrentArray#writeLock()}.
 *
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockArraySet<E> extends ConcurrentReentrantRWLockArray<E> {

    private static final long serialVersionUID = -3394386864246350866L;

    public ConcurrentAtomicARSWLockArraySet(final Class<E> type) {
        super(type);
    }

    public ConcurrentAtomicARSWLockArraySet(final Class<E> type, final int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull final E element) {
        return !contains(element) && super.add(element);
    }
}
