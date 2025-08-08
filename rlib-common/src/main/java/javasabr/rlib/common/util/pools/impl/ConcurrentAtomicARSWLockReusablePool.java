package javasabr.rlib.common.util.pools.impl;

import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.common.util.array.ConcurrentArray;
import javasabr.rlib.common.util.array.impl.ConcurrentAtomicARSWLockArray;
import javasabr.rlib.common.util.pools.Reusable;
import javasabr.rlib.common.util.pools.ReusablePool;
import org.jspecify.annotations.NullMarked;

/**
 * The threadsafe implementation of the {@link ReusablePool} using like a storage the
 * {@link ConcurrentAtomicARSWLockArray}*.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public class ConcurrentAtomicARSWLockReusablePool<E extends Reusable> extends ConcurrentReusablePool<E> {

  public ConcurrentAtomicARSWLockReusablePool(Class<? super E> type) {
    super(type);
  }

  @Override
  protected ConcurrentArray<E> createPool(Class<? super E> type) {
    return ArrayFactory.newConcurrentAtomicARSWLockArray(type);
  }
}
