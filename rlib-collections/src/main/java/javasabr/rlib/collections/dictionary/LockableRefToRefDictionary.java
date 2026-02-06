package javasabr.rlib.collections.dictionary;

import javasabr.rlib.collections.operation.LockableOperations;
import javasabr.rlib.collections.operation.LockableSource;
import javasabr.rlib.common.ThreadSafe;

/**
 * A thread-safe mutable dictionary that provides locking operations for concurrent access.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @since 10.0.0
 */
public interface LockableRefToRefDictionary<K, V> extends MutableRefToRefDictionary<K, V>,
    LockableSource, ThreadSafe {

  /**
   * Returns lockable operations for performing thread-safe operations on this dictionary.
   *
   * @return lockable operations
   * @since 10.0.0
   */
  LockableOperations<LockableRefToRefDictionary<K, V>> operations();
}
