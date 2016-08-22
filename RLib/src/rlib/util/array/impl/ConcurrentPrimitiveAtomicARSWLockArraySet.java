package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

import rlib.concurrent.lock.LockFactory;
import rlib.util.array.ConcurrentArray;

/**
 * The concurrent implementation of the array without duplications using {@link
 * LockFactory#newARSWLock()} for {@link ConcurrentArray#readLock()} and {@link
 * ConcurrentArray#writeLock()}.
 *
 * @author JavaSaBr
 */
public class ConcurrentPrimitiveAtomicARSWLockArraySet<E> extends ConcurrentReentrantRWLockArray<E> {

    private static final long serialVersionUID = -3394386864246350866L;

    public ConcurrentPrimitiveAtomicARSWLockArraySet(final Class<E> type) {
        super(type);
    }

    public ConcurrentPrimitiveAtomicARSWLockArraySet(final Class<E> type, final int size) {
        super(type, size);
    }

    @NotNull
    @Override
    public AbstractConcurrentArray<E> add(@NotNull final E element) {
        return contains(element) ? this : super.add(element);
    }
}
