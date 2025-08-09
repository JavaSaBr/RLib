package javasabr.rlib.common.util.pools.impl;

import javasabr.rlib.common.util.pools.Reusable;
import javasabr.rlib.common.util.pools.ReusablePool;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of the {@link ReusablePool} using synchronization for take/put methods.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public class SynchronizedReusablePool<E extends Reusable> extends FastReusablePool<E> {

  public SynchronizedReusablePool(Class<? super E> type) {
    super(type);
  }

  @Override
  public synchronized boolean isEmpty() {
    return super.isEmpty();
  }

  @Override
  public synchronized void put(E object) {
    super.put(object);
  }

  @Override
  public synchronized void remove(E object) {
    super.remove(object);
  }

  @Override
  public synchronized @Nullable E take() {
    return super.take();
  }
}
