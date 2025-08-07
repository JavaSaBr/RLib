package javasabr.rlib.common.util.pools.impl;

import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.common.util.array.ConcurrentArray;
import javasabr.rlib.common.util.array.impl.ConcurrentAtomicARSWLockArray;
import javasabr.rlib.common.util.pools.Pool;
import org.jetbrains.annotations.NotNull;

/**
 * The threadsafe implementation of the {@link Pool} using like a storage the {@link
 * ConcurrentAtomicARSWLockArray}*.
 *
 * @param <E> the object's type.
 * @author JavaSaBr
 */
public class ConcurrentAtomicARSWLockPool<E> extends ConcurrentPool<E> {

    public ConcurrentAtomicARSWLockPool(@NotNull Class<? super E> type) {
        super(type);
    }

    @Override
    protected @NotNull ConcurrentArray<E> createPool(@NotNull Class<? super E> type) {
        return ArrayFactory.newConcurrentAtomicARSWLockArray(type);
    }
}
