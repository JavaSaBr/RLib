package javasabr.rlib.common.util.pools.impl;

import javasabr.rlib.common.util.pools.Pool;
import javasabr.rlib.common.util.pools.Reusable;
import javasabr.rlib.common.util.pools.ReusablePool;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The base concurrent implementation of the {@link Pool} for {@link Reusable} objects.
 *
 * @param <E> the object's type.
 * @author JavaSaBr
 */
@NullMarked
public abstract class ConcurrentReusablePool<E extends Reusable> extends ConcurrentPool<E> implements ReusablePool<E> {

  public ConcurrentReusablePool(Class<? super E> type) {
    super(type);
  }

  @Override
  public void put(E object) {
    object.free();
    super.put(object);
  }

  @Override
  public @Nullable E take() {

    E object = super.take();

    if (object != null) {
      object.reuse();
    }

    return object;
  }
}
