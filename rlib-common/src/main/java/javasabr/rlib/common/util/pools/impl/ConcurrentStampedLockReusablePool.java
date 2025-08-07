package javasabr.rlib.common.util.pools.impl;

import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.common.util.array.ConcurrentArray;
import javasabr.rlib.common.util.array.impl.ConcurrentStampedLockArray;
import javasabr.rlib.common.util.pools.Reusable;
import javasabr.rlib.common.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * The threadsafe implementation of the {@link ReusablePool} using like a storage the
 * {@link ConcurrentStampedLockArray}*.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public class ConcurrentStampedLockReusablePool<E extends Reusable> extends ConcurrentReusablePool<E> {

  public ConcurrentStampedLockReusablePool(Class<? super E> type) {
    super(type);
  }

  @Override
  protected ConcurrentArray<E> createPool(Class<? super E> type) {
    return ArrayFactory.newConcurrentStampedLockArray(type);
  }
}
