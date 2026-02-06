package javasabr.rlib.collections.array;

import javasabr.rlib.collections.operation.LockableOperations;
import javasabr.rlib.collections.operation.LockableSource;
import javasabr.rlib.common.ThreadSafe;

/**
 * A thread-safe mutable array that provides locking operations for concurrent access.
 *
 * @param <E> the type of elements in this array
 * @since 10.0.0
 */
public interface LockableArray<E> extends MutableArray<E>, LockableSource, ThreadSafe {

  /**
   * Returns lockable operations for performing thread-safe operations on this array.
   *
   * @return lockable operations
   * @since 10.0.0
   */
  LockableOperations<LockableArray<E>> operations();
}
