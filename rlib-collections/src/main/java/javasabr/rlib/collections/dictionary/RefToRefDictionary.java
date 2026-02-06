package javasabr.rlib.collections.dictionary;

import java.util.function.BiConsumer;
import javasabr.rlib.collections.dictionary.impl.ImmutableHashBasedRefToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.SimpleRefToRefEntry;

/**
 * A dictionary that maps reference keys to reference values.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @since 10.0.0
 */
public interface RefToRefDictionary<K, V> extends Dictionary<K, V> {

  /**
   * Creates a new builder for constructing an immutable dictionary.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @return a new builder
   * @since 10.0.0
   */
  static <K, V> RefToRefDictionaryBuilder<K, V> builder() {
    return new RefToRefDictionaryBuilder<>();
  }

  /**
   * Creates a new builder for constructing an immutable dictionary with explicit types.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @param keyType the key type (unused, for type inference)
   * @param valueType the value type (unused, for type inference)
   * @return a new builder
   * @since 10.0.0
   */
  static <K, V> RefToRefDictionaryBuilder<K, V> builder(
      Class<? super K> keyType, 
      Class<? super V> valueType) {
    return new RefToRefDictionaryBuilder<>();
  }

  /**
   * Creates a builder and adds the first key-value pair.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @param key the first key
   * @param value the first value
   * @return a builder with the entry added
   * @since 10.0.0
   */
  static <K, V> RefToRefDictionaryBuilder<K, V> startWith(K key, V value) {
    return new RefToRefDictionaryBuilder<K, V>()
        .put(key, value);
  }

  /**
   * Creates a new entry with the specified key and value.
   *
   * @param <K> the type of the key
   * @param <V> the type of the value
   * @param key the key
   * @param value the value
   * @return a new entry
   * @since 10.0.0
   */
  static <K, V> RefToRefEntry<K, V> entry(K key, V value) {
    return new SimpleRefToRefEntry<>(key, value);
  }

  /**
   * Returns an empty immutable reference-to-reference dictionary.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @return an empty dictionary
   * @since 10.0.0
   */
  static <K, V> RefToRefDictionary<K, V> empty() {
    return ImmutableHashBasedRefToRefDictionary.empty();
  }

  /**
   * Creates an immutable dictionary with a single mapping.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @param key the key
   * @param value the value
   * @return an immutable dictionary
   * @since 10.0.0
   */
  static <K, V> RefToRefDictionary<K, V> of(K key, V value) {
    return ofEntries(entry(key, value));
  }

  /**
   * Creates an immutable dictionary with two mappings.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @param k1 the first key
   * @param v1 the first value
   * @param k2 the second key
   * @param v2 the second value
   * @return an immutable dictionary
   * @since 10.0.0
   */
  static <K, V> RefToRefDictionary<K, V> of(K k1, V v1, K k2, V v2) {
    return ofEntries(entry(k1, v1), entry(k2, v2));
  }

  /**
   * Creates an immutable dictionary with three mappings.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @param k1 the first key
   * @param v1 the first value
   * @param k2 the second key
   * @param v2 the second value
   * @param k3 the third key
   * @param v3 the third value
   * @return an immutable dictionary
   * @since 10.0.0
   */
  static <K, V> RefToRefDictionary<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
    return ofEntries(entry(k1, v1), entry(k2, v2), entry(k3, v3));
  }

  /**
   * Creates an immutable dictionary with four mappings.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @param k1 the first key
   * @param v1 the first value
   * @param k2 the second key
   * @param v2 the second value
   * @param k3 the third key
   * @param v3 the third value
   * @param k4 the fourth key
   * @param v4 the fourth value
   * @return an immutable dictionary
   * @since 10.0.0
   */
  static <K, V> RefToRefDictionary<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    return ofEntries(entry(k1, v1), entry(k2, v2), entry(k3, v3), entry(k4, v4));
  }

  /**
   * Creates an immutable dictionary from entries.
   *
   * @param <K> the type of keys
   * @param <V> the type of values
   * @param entries the entries
   * @return an immutable dictionary
   * @since 10.0.0
   */
  @SafeVarargs
  static <K, V> RefToRefDictionary<K, V> ofEntries(RefToRefEntry<K, V>... entries) {
    MutableRefToRefDictionary<K, V> mutable = DictionaryFactory.mutableRefToRefDictionary();
    for (var entry : entries) {
      mutable.put(entry.key(), entry.value());
    }
    return mutable.toReadOnly();
  }

  /**
   * Performs the given action for each key-value pair.
   *
   * @param consumer the action to perform
   * @since 10.0.0
   */
  void forEach(BiConsumer<K, V> consumer);
}
