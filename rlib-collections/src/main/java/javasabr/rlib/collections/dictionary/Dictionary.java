package javasabr.rlib.collections.dictionary;

import java.util.Collection;
import java.util.Optional;
import javasabr.rlib.collections.array.Array;
import javasabr.rlib.collections.array.MutableArray;
import org.jspecify.annotations.Nullable;

/**
 * A base interface for dictionary (map-like) data structures.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface Dictionary<K, V> extends Iterable<V> {

  /**
   * Returns whether this dictionary contains the specified key.
   *
   * @param key the key to check
   * @return true if the key is present
   * @since 10.0.0
   */
  boolean containsKey(K key);

  /**
   * Returns whether this dictionary contains the specified value.
   *
   * @param value the value to check
   * @return true if the value is present
   * @since 10.0.0
   */
  boolean containsValue(V value);

  /**
   * Returns whether this dictionary is empty.
   *
   * @return true if empty
   * @since 10.0.0
   */
  boolean isEmpty();

  /**
   * Returns the number of key-value mappings.
   *
   * @return the number of mappings
   * @since 10.0.0
   */
  int size();

  /**
   * Returns the value associated with the specified key.
   *
   * @param key the key
   * @return the value or null if not found
   * @since 10.0.0
   */
  @Nullable
  V get(K key);

  /**
   * Returns an optional containing the value for the specified key.
   *
   * @param key the key
   * @return an optional with the value
   * @since 10.0.0
   */
  Optional<V> getOptional(K key);

  /**
   * Returns the value for the key, or a default value if not found.
   *
   * @param key the key
   * @param def the default value
   * @return the value or the default
   * @since 10.0.0
   */
  V getOrDefault(K key, V def);

  /**
   * Collects all keys into the provided collection.
   *
   * @param <C> the collection type
   * @param container the collection to add keys to
   * @return the container with added keys
   * @since 10.0.0
   */
  <C extends Collection<K>> C keys(C container);

  /**
   * Collects all keys into the provided mutable array.
   *
   * @param container the array to add keys to
   * @return the container with added keys
   * @since 10.0.0
   */
  MutableArray<K> keys(MutableArray<K> container);

  /**
   * Returns all keys as an immutable array.
   *
   * @param type the type of keys
   * @return an array of keys
   * @since 10.0.0
   */
  Array<K> keys(Class<K> type);

  /**
   * Collects all values into the provided collection.
   *
   * @param <C> the collection type
   * @param container the collection to add values to
   * @return the container with added values
   * @since 10.0.0
   */
  <C extends Collection<V>> C values(C container);

  /**
   * Collects all values into the provided mutable array.
   *
   * @param container the array to add values to
   * @return the container with added values
   * @since 10.0.0
   */
  MutableArray<V> values(MutableArray<V> container);

  /**
   * Collects part of values from this dictionary with starting from index with provided soft limit.
   *
   * @param container the array to add values to
   * @param startIndex the index of first value to collect
   * @param limit the soft limit of values to collect
   * @return the index which can be used as startIndex for next iteration or -1
   * @since 10.0.0
   */
  int values(MutableArray<V> container, int startIndex, int limit);
  
  /**
   * Returns all values as an immutable array.
   *
   * @param type the type of values
   * @return an array of values
   * @since 10.0.0
   */
  Array<V> values(Class<V> type);
}
