package javasabr.rlib.collections.dictionary;

import java.util.Optional;
import javasabr.rlib.collections.array.LongArray;
import javasabr.rlib.collections.array.MutableLongArray;
import javasabr.rlib.collections.dictionary.impl.ImmutableHashBasedLongToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.SimpleLongToRefEntry;
import javasabr.rlib.functions.LongObjConsumer;
import org.jspecify.annotations.Nullable;

/**
 * A dictionary that maps long keys to reference values.
 *
 * @param <V> the type of values
 * @since 10.0.0
 */
public interface LongToRefDictionary<V> extends Dictionary<Long, V> {

  /**
   * Creates a new entry with the specified key and value.
   *
   * @param <V> the type of the value
   * @param key the key
   * @param value the value
   * @return a new entry
   * @since 10.0.0
   */
  static <V> LongToRefEntry<V> entry(long key, V value) {
    return new SimpleLongToRefEntry<>(key, value);
  }

  /**
   * Returns an empty immutable long-to-reference dictionary.
   *
   * @param <V> the type of values
   * @return an empty dictionary
   * @since 10.0.0
   */
  static <V> LongToRefDictionary<V> empty() {
    return ImmutableHashBasedLongToRefDictionary.empty();
  }

  /**
   * Creates an immutable dictionary with a single mapping.
   *
   * @param <V> the type of values
   * @param key the key
   * @param value the value
   * @return an immutable dictionary
   * @since 10.0.0
   */
  static <V> LongToRefDictionary<V> of(long key, V value) {
    return ofEntries(entry(key, value));
  }

  /**
   * Creates an immutable dictionary with two mappings.
   *
   * @param <V> the type of values
   * @param k1 the first key
   * @param v1 the first value
   * @param k2 the second key
   * @param v2 the second value
   * @return an immutable dictionary
   * @since 10.0.0
   */
  static <V> LongToRefDictionary<V> of(long k1, V v1, long k2, V v2) {
    return ofEntries(entry(k1, v1), entry(k2, v2));
  }

  /**
   * Creates an immutable dictionary from entries.
   *
   * @param <V> the type of values
   * @param entries the entries
   * @return an immutable dictionary
   * @since 10.0.0
   */
  @SafeVarargs
  static <V> LongToRefDictionary<V> ofEntries(LongToRefEntry<V>... entries) {
    MutableLongToRefDictionary<V> mutable = DictionaryFactory.mutableLongToRefDictionary();
    for (var entry : entries) {
      mutable.put(entry.key(), entry.value());
    }
    return mutable.toReadOnly();
  }

  /**
   * Returns whether this dictionary contains the specified key.
   *
   * @param key the key to check
   * @return true if the key is present
   * @since 10.0.0
   */
  boolean containsKey(long key);

  /**
   * Returns the value for the specified key.
   *
   * @param key the key
   * @return the value or null if not found
   * @since 10.0.0
   */
  @Nullable
  V get(long key);

  /**
   * Returns an optional containing the value for the specified key.
   *
   * @param key the key
   * @return an optional with the value
   * @since 10.0.0
   */
  Optional<V> getOptional(long key);

  /**
   * Returns the value for the key, or a default value if not found.
   *
   * @param key the key
   * @param def the default value
   * @return the value or the default
   * @since 10.0.0
   */
  V getOrDefault(long key, V def);

  /**
   * Collects all keys into the provided mutable long array.
   *
   * @param container the array to add keys to
   * @return the container with added keys
   * @since 10.0.0
   */
  MutableLongArray keys(MutableLongArray container);

  /**
   * Returns all keys as an immutable long array.
   *
   * @return an array of keys
   * @since 10.0.0
   */
  LongArray keys();

  /**
   * Performs the given action for each key-value pair.
   *
   * @param consumer the action to perform
   * @since 10.0.0
   */
  void forEach(LongObjConsumer<V> consumer);
}
