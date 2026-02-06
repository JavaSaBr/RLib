package javasabr.rlib.collections.dictionary;

import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedIntToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedLongToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedRefToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.StampedLockBasedHashBasedRefToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.gc.optimized.GcOptimizedMutableHashBasedIntToRefDictionary;
import lombok.experimental.UtilityClass;

/**
 * Factory for creating various dictionary implementations.
 *
 * @since 10.0.0
 */
@UtilityClass
public class DictionaryFactory {

  /**
   * Creates a new mutable reference-to-reference dictionary.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @return a new mutable dictionary
   * @since 10.0.0
   */
  public static <K, V> MutableRefToRefDictionary<K, V> mutableRefToRefDictionary() {
    return new DefaultMutableHashBasedRefToRefDictionary<>();
  }

  /**
   * Creates a new mutable int-to-reference dictionary.
   *
   * @param <V> the type of values
   * @return a new mutable dictionary
   * @since 10.0.0
   */
  public static <V> MutableIntToRefDictionary<V> mutableIntToRefDictionary() {
    return new DefaultMutableHashBasedIntToRefDictionary<>();
  }

  /**
   * Creates a new GC-optimized mutable int-to-reference dictionary.
   *
   * @param <V> the type of values
   * @return a new GC-optimized mutable dictionary
   * @since 10.0.0
   */
  public static <V> MutableIntToRefDictionary<V> gcOptimizedIntToRefDictionary() {
    return new GcOptimizedMutableHashBasedIntToRefDictionary<>();
  }

  /**
   * Creates a new mutable long-to-reference dictionary.
   *
   * @param <V> the type of values
   * @return a new mutable dictionary
   * @since 10.0.0
   */
  public static <V> MutableLongToRefDictionary<V> mutableLongToRefDictionary() {
    return new DefaultMutableHashBasedLongToRefDictionary<>();
  }

  /**
   * Creates a new mutable reference-to-reference dictionary with explicit types.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @param keyType the key type (unused, for type inference)
   * @param valueType the value type (unused, for type inference)
   * @return a new mutable dictionary
   * @since 10.0.0
   */
  public static <K, V> MutableRefToRefDictionary<K, V> mutableRefToRefDictionary(
      Class<? super K> keyType,
      Class<? super V> valueType) {
    return new DefaultMutableHashBasedRefToRefDictionary<>();
  }

  /**
   * Creates a new thread-safe dictionary backed by a stamped lock.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @return a new lockable dictionary
   * @since 10.0.0
   */
  public static <K, V> LockableRefToRefDictionary<K, V> stampedLockBasedRefToRefDictionary() {
    return new StampedLockBasedHashBasedRefToRefDictionary<>();
  }

  /**
   * Creates a new thread-safe dictionary backed by a stamped lock with explicit types.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @param keyType the key type (unused, for type inference)
   * @param valueType the value type (unused, for type inference)
   * @return a new lockable dictionary
   * @since 10.0.0
   */
  public static <K, V> LockableRefToRefDictionary<K, V> stampedLockBasedRefToRefDictionary(
      Class<? super K> keyType,
      Class<? super V> valueType) {
    return new StampedLockBasedHashBasedRefToRefDictionary<>();
  }
}
