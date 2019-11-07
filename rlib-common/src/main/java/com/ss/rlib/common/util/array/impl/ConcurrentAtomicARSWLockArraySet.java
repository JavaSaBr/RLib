package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.lock.LockFactory;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

/**
 * The concurrent implementation of the array without duplications using {@link LockFactory#newAtomicARSWLock()} for
 * {@link ConcurrentArray#readLock()} and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockArraySet<E> extends ConcurrentAtomicARSWLockArray<E> {

    private static final long serialVersionUID = -3394386864246350866L;

    public ConcurrentAtomicARSWLockArraySet(@NotNull Class<E> type) {
        super(type);
    }

    public ConcurrentAtomicARSWLockArraySet(@NotNull Class<E> type, int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull E element) {
        return !contains(element) && super.add(element);
    }

    @Override
    protected void processAdd(@NotNull Array<? extends E> elements, int selfSize, int targetSize) {
        var array = elements.array();
        for (int i = 0, length = elements.size(); i < length; i++) {
            E element = array[i];
            if (!contains(element)) {
                unsafeAdd(element);
            }
        }
    }

    @Override
    protected void processAdd(@NotNull E[] elements, int selfSize, int targetSize) {
        for (E element : elements) {
            if (!contains(element)) {
                unsafeAdd(element);
            }
        }
    }
}
