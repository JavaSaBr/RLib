package javasabr.rlib.common.util.pools;

import javasabr.rlib.common.concurrent.lock.impl.AtomicReadWriteLock;
import javasabr.rlib.common.concurrent.lock.impl.ReentrantARSWLock;
import javasabr.rlib.common.util.pools.impl.ConcurrentAtomicARSWLockPool;
import javasabr.rlib.common.util.pools.impl.ConcurrentAtomicARSWLockReusablePool;
import javasabr.rlib.common.util.pools.impl.ConcurrentReentrantRWLockPool;
import javasabr.rlib.common.util.pools.impl.ConcurrentStampedLockPool;
import javasabr.rlib.common.util.pools.impl.ConcurrentStampedLockReusablePool;
import javasabr.rlib.common.util.pools.impl.FastPool;
import javasabr.rlib.common.util.pools.impl.FastReusablePool;
import javasabr.rlib.common.util.pools.impl.SynchronizedReusablePool;
import org.jspecify.annotations.NullMarked;

/**
 * The factory for creating new pools.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class PoolFactory {

  /**
   * Create a reusable pool based on {@link AtomicReadWriteLock}.
   *
   * @param type the object's type.
   * @param <T> the object's type.
   * @return the reusable pool.
   */
  public static <T extends Reusable> ReusablePool<T> newConcurrentAtomicARSWLockReusablePool(
      Class<? super T> type) {
    return new ConcurrentAtomicARSWLockReusablePool<>(type);
  }

  /**
   * Create a reusable pool based on {@link java.util.concurrent.locks.StampedLock}.
   *
   * @param type the object's type.
   * @param <T> the object's type.
   * @return the reusable pool.
   */
  public static <T extends Reusable> ReusablePool<T> newConcurrentStampedLockReusablePool(
      Class<? super T> type) {
    return new ConcurrentStampedLockReusablePool<>(type);
  }

  /**
   * Create a reusable pool based on {@link ReentrantARSWLock}.
   *
   * @param type the object's type.
   * @param <T> the object's type.
   * @return the reusable pool.
   */
  public static <T extends Reusable> ReusablePool<T> newConcurrentReentrantRWLockReusablePool(
      Class<? super T> type) {
    return new ConcurrentReentrantRWLockPool<>(type);
  }

  /**
   * Create a reusable pool based on synchronization block.
   *
   * @param type the object's type.
   * @param <T> the object's type.
   * @return the reusable pool.
   */
  public static <T extends Reusable> ReusablePool<T> newSynchronizedReusablePool(
      Class<? super T> type) {
    return new SynchronizedReusablePool<>(type);
  }

  /**
   * Create a reusable pool.
   *
   * @param type the object's type.
   * @param <T> the object's type.
   * @return the reusable pool.
   */
  public static <T extends Reusable> ReusablePool<T> newReusablePool(Class<? super T> type) {
    return new FastReusablePool<>(type);
  }

  /**
   * Create a pool based on {@link ReentrantARSWLock}.
   *
   * @param type the object's type.
   * @param <T> the object's type.
   * @return the pool.
   */
  public static <T> Pool<T> newConcurrentAtomicARSWLockPool(Class<? super T> type) {
    return new ConcurrentAtomicARSWLockPool<>(type);
  }

  /**
   * Create a pool based on {@link java.util.concurrent.locks.StampedLock}.
   *
   * @param type the object's type.
   * @param <T> the object's type.
   * @return the pool.
   */
  public static <T> Pool<T> newConcurrentStampedLockPool(Class<? super T> type) {
    return new ConcurrentStampedLockPool<>(type);
  }

  /**
   * Create a pool.
   *
   * @param type the object's type.
   * @param <T> the object's type.
   * @return the reusable pool.
   */
  public static <T> Pool<T> newPool(Class<? super T> type) {
    return new FastPool<>(type);
  }

  private PoolFactory() {
    throw new IllegalArgumentException();
  }
}
