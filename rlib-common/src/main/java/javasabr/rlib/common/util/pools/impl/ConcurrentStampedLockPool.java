package javasabr.rlib.common.util.pools.impl;

import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.common.util.array.ConcurrentArray;
import javasabr.rlib.common.util.array.impl.ConcurrentStampedLockArray;
import javasabr.rlib.common.util.pools.Pool;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * The threadsafe implementation of the {@link Pool} using like a storage the {@link ConcurrentStampedLockArray}*.
 *
 * @param <E> the object's type.
 * @author JavaSaBr
 */
@NullMarked
public class ConcurrentStampedLockPool<E> extends ConcurrentPool<E> {

  public ConcurrentStampedLockPool(Class<? super E> type) {
    super(type);
  }

  @Override
  protected ConcurrentArray<E> createPool(Class<? super E> type) {
    return ArrayFactory.newConcurrentStampedLockArray(type);
  }
}
