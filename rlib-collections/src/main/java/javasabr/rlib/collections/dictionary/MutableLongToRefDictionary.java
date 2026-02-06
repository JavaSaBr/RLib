package javasabr.rlib.collections.dictionary;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;
import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedLongToRefDictionary;
import org.jspecify.annotations.Nullable;

/**
 * A mutable dictionary that maps long keys to reference values.
 *
 * @param <V> the type of values
 * @since 10.0.0
 */
public interface MutableLongToRefDictionary<V> extends LongToRefDictionary<V> {

  /**
   * Creates a new mutable long-to-reference dictionary.
   *
   * @param <V> the type of values
   * @param valueType the value type (unused, for type inference)
   * @return a new mutable dictionary
   * @since 10.0.0
   */
  static <V> MutableLongToRefDictionary<V> ofTypes(Class<V> valueType) {
    return new DefaultMutableHashBasedLongToRefDictionary<>();
  }

  /**
   * Returns the value for the key, computing it if absent.
   *
   * @param key the key
   * @param factory the factory to compute the value
   * @return the existing or computed value
   * @since 10.0.0
   */
  V getOrCompute(long key, Supplier<V> factory);

  /**
   * Returns the value for the key, computing it if absent.
   *
   * @param key the key
   * @param factory the factory to compute the value from the key
   * @return the existing or computed value
   * @since 10.0.0
   */
  V getOrCompute(long key, LongFunction<V> factory);

  /**
   * Returns the value for the key, computing it if absent.
   *
   * @param <T> the type of the factory argument
   * @param key the key
   * @param arg1 the argument to pass to the factory
   * @param factory the factory to compute the value
   * @return the existing or computed value
   * @since 10.0.0
   */
  <T> V getOrCompute(long key, T arg1, Function<T, V> factory);

  /**
   * Associates the value with the key.
   *
   * @param key the key
   * @param value the value
   * @return the previous value for the key or null
   * @since 10.0.0
   */
  @Nullable
  V put(long key, V value);

  /**
   * Associates the value with the key if not already present.
   *
   * @param key the key
   * @param value the value
   * @return the existing value if present, or null if added
   * @since 10.0.0
   */
  @Nullable
  V putIfAbsent(long key, V value);

  /**
   * Copies all mappings from the specified dictionary.
   *
   * @param dictionary the dictionary to copy from
   * @since 10.0.0
   */
  void putAll(LongToRefDictionary<? extends V> dictionary);

  /**
   * Copies all mappings from the specified dictionary and returns this.
   *
   * @param dictionary the dictionary to copy from
   * @return this dictionary
   * @since 10.0.0
   */
  MutableLongToRefDictionary<V> append(LongToRefDictionary<? extends V> dictionary);

  /**
   * Associates the value with the key and returns an optional of the previous value.
   *
   * @param key the key
   * @param value the value
   * @return an optional of the previous value
   * @since 10.0.0
   */
  Optional<V> putOptional(long key, V value);

  /**
   * Removes the mapping for the key.
   *
   * @param key the key
   * @return the previous value for the key or null
   * @since 10.0.0
   */
  @Nullable
  V remove(long key);

  /**
   * Removes the mapping for the key only if it maps to the expected value.
   *
   * @param key the key
   * @param expectedValue the expected value
   * @return true if the expected value was removed
   * @since 10.0.0
   */
  boolean remove(long key, V expectedValue);

  /**
   * Removes the mapping for the key and returns an optional of the previous value.
   *
   * @param key the key
   * @return an optional of the previous value
   * @since 10.0.0
   */
  Optional<V> removeOptional(long key);

  /**
   * Removes all mappings from this dictionary.
   *
   * @since 10.0.0
   */
  void clear();

  /**
   * Returns an immutable copy of this dictionary.
   *
   * @return an immutable dictionary
   * @since 10.0.0
   */
  LongToRefDictionary<V> toReadOnly();
}
