package javasabr.rlib.collections.dictionary;

import java.util.Objects;
import java.util.Optional;
import javasabr.rlib.collections.array.IntArray;
import javasabr.rlib.collections.array.MutableIntArray;
import javasabr.rlib.collections.dictionary.impl.ImmutableHashBasedIntToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.SimpleIntToRefEntry;
import javasabr.rlib.functions.IntObjConsumer;
import org.jspecify.annotations.Nullable;

/**
 * A dictionary that maps int keys to reference values.
 *
 * @param <V> the type of values
 * @since 10.0.0
 */
public interface IntToRefDictionary<V> extends Dictionary<Integer, V> {

  /**
   * Creates a new entry with the specified key and value.
   *
   * @param <V> the type of the value
   * @param key the key
   * @param value the value
   * @return a new entry
   * @since 10.0.0
   */
  static <V> IntToRefEntry<V> entry(int key, V value) {
    return new SimpleIntToRefEntry<>(key, value);
  }

  /**
   * Returns an empty immutable int-to-reference dictionary.
   *
   * @param <V> the type of values
   * @return an empty dictionary
   * @since 10.0.0
   */
  static <V> IntToRefDictionary<V> empty() {
    return ImmutableHashBasedIntToRefDictionary.empty();
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
  static <V> IntToRefDictionary<V> of(int key, V value) {
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
  static <V> IntToRefDictionary<V> of(int k1, V v1, int k2, V v2) {
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
  static <V> IntToRefDictionary<V> ofEntries(IntToRefEntry<V>... entries) {
    MutableIntToRefDictionary<V> mutable = DictionaryFactory.mutableIntToRefDictionary();
    for (var entry : entries) {
      mutable.put(entry.key(), Objects.requireNonNull(entry.value()));
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
  boolean containsKey(int key);

  /**
   * Returns the value for the specified key.
   *
   * @param key the key
   * @return the value or null if not found
   * @since 10.0.0
   */
  @Nullable
  V get(int key);

  /**
   * Returns an optional containing the value for the specified key.
   *
   * @param key the key
   * @return an optional with the value
   * @since 10.0.0
   */
  Optional<V> getOptional(int key);

  /**
   * Returns the value for the key, or a default value if not found.
   *
   * @param key the key
   * @param def the default value
   * @return the value or the default
   * @since 10.0.0
   */
  V getOrDefault(int key, V def);

  /**
   * Collects all keys into the provided mutable int array.
   *
   * @param container the array to add keys to
   * @return the container with added keys
   * @since 10.0.0
   */
  MutableIntArray keys(MutableIntArray container);

  /**
   * Returns all keys as an immutable int array.
   *
   * @return an array of keys
   * @since 10.0.0
   */
  IntArray keys();

  /**
   * Performs the given action for each key-value pair.
   *
   * @param consumer the action to perform
   * @since 10.0.0
   */
  void forEach(IntObjConsumer<V> consumer);
}
