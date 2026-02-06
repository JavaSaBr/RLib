package javasabr.rlib.collections.dictionary;

import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * A builder for constructing immutable {@link RefToRefDictionary} instances.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @since 10.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefToRefDictionaryBuilder<K, V> {
  
  MutableRefToRefDictionary<K, V> elements;

  /**
   * Creates a new dictionary builder.
   *
   * @since 10.0.0
   */
  public RefToRefDictionaryBuilder() {
    this.elements = DictionaryFactory.mutableRefToRefDictionary();
  }

  /**
   * Adds a key-value mapping to the dictionary being built.
   *
   * @param key the key
   * @param value the value
   * @return this builder for method chaining
   * @since 10.0.0
   */
  public RefToRefDictionaryBuilder<K, V> put(K key, V value) {
    this.elements.put(key, value);
    return this;
  }

  /**
   * Adds all mappings from another dictionary to the dictionary being built.
   *
   * @param other the dictionary to add mappings from
   * @return this builder for method chaining
   * @since 10.0.0
   */
  public RefToRefDictionaryBuilder<K, V> put(RefToRefDictionary<K, V> other) {
    this.elements.putAll(other);
    return this;
  }

  /**
   * Adds all mappings from a map to the dictionary being built.
   *
   * @param other the map to add mappings from
   * @return this builder for method chaining
   * @since 10.0.0
   */
  public RefToRefDictionaryBuilder<K, V> put(Map<K, V> other) {
    for (Map.Entry<K, V> entry : other.entrySet()) {
      elements.put(entry.getKey(), entry.getValue());
    }
    return this;
  }

  /**
   * Builds and returns an immutable dictionary containing all added mappings.
   *
   * @return an immutable dictionary
   * @since 10.0.0
   */
  public RefToRefDictionary<K, V> build() {
    return elements.toReadOnly();
  }
}
