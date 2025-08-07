package javasabr.rlib.common.util.pools.impl;

import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.common.util.array.ConcurrentArray;
import javasabr.rlib.common.util.array.impl.ConcurrentAtomicARSWLockArray;
import javasabr.rlib.common.util.pools.Pool;
import org.jspecify.annotations.NullMarked;

/**
 * The threadsafe implementation of the {@link Pool} using like a storage the {@link ConcurrentAtomicARSWLockArray}*.
 *
 * @param <E> the object's type.
 * @author JavaSaBr
 */
@NullMarked
public class ConcurrentAtomicARSWLockPool<E> extends ConcurrentPool<E> {

  public ConcurrentAtomicARSWLockPool(Class<? super E> type) {
    super(type);
  }

  @Override
  protected ConcurrentArray<E> createPool(Class<? super E> type) {
    return ArrayFactory.newConcurrentAtomicARSWLockArray(type);
  }
}
