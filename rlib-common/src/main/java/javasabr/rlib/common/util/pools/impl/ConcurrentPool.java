package javasabr.rlib.common.util.pools.impl;

import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ConcurrentArray;
import javasabr.rlib.common.util.pools.Pool;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The base concurrent implementation of the {@link Pool}.
 *
 * @param <E> the object's type.
 * @author JavaSaBr
 */
@NullMarked
public abstract class ConcurrentPool<E> implements Pool<E> {

  /**
   * The storage of objects.
   */
  protected final ConcurrentArray<E> pool;

  public ConcurrentPool(Class<? super E> type) {
    this.pool = createPool(type);
  }

  protected abstract ConcurrentArray<E> createPool(Class<? super E> type);

  @Override
  public boolean isEmpty() {
    return pool.isEmpty();
  }

  @Override
  public void put(E object) {
    pool.runInWriteLock(object, Array::add);
  }

  @Override
  public void remove(E object) {
    pool.runInWriteLock(object, Array::fastRemove);
  }

  @Override
  public @Nullable E take() {

    if (pool.isEmpty()) {
      return null;
    }

    E object = pool.getInWriteLock(Array::pop);

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
