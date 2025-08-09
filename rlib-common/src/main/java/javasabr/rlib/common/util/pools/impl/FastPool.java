package javasabr.rlib.common.util.pools.impl;

import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.pools.Pool;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The fast implementation of the {@link Pool}. It isn't threadsafe.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public class FastPool<E> implements Pool<E> {

  /**
   * The storage of objects.
   */
  private final Array<E> pool;

  public FastPool(Class<? super E> type) {
    this.pool = Array.ofType(type);
  }

  @Override
  public boolean isEmpty() {
    return pool.isEmpty();
  }

  @Override
  public void put(E object) {
    pool.add(object);
  }

  @Override
  public void remove(E object) {
    pool.fastRemove(object);
  }

  @Override
  public @Nullable E take() {

    E object = pool.pop();

    if (object == null) {
      return null;
    }

    return object;
  }

  @Override
  public String toString() {
    return pool.toString();
  }
}
