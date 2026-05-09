package javasabr.rlib.reusable.pool;

import javasabr.rlib.reusable.Reusable;
import javasabr.rlib.reusable.pool.impl.ArrayBasedPool;
import javasabr.rlib.reusable.pool.impl.ArrayBasedReusablePool;
import javasabr.rlib.reusable.pool.impl.LockableArrayBasePool;

/**
 * Factory for creating pool instances.
 *
 * @since 10.0.0
 */
public class PoolFactory {

  /**
   * Creates a new array-based pool.
   *
   * @param <T> the element type
   * @param type the element class
   * @return the new pool
   * @since 10.0.0
   */
  public static <T> Pool<T> newPool(Class<? super T> type) {
    return new ArrayBasedPool<>(type);
  }

  /**
   * Creates a new array-based pool for reusable objects.
   *
   * @param <T> the reusable element type
   * @param type the element class
   * @return the new reusable pool
   * @since 10.0.0
   */
  public static <T extends Reusable> ReusablePool<T> newReusablePool(Class<? super T> type) {
    return new ArrayBasedReusablePool<>(type);
  }

  /**
   * Creates a new lock-based pool for thread-safe access.
   *
   * @param <T> the element type
   * @param type the element class
   * @return the new lock-based pool
   * @since 10.0.0
   */
  public static <T> Pool<T> newLockBasePool(Class<? super T> type) {
    return new LockableArrayBasePool<>(type);
  }
}
