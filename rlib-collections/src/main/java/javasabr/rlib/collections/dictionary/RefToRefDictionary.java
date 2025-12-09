package javasabr.rlib.collections.dictionary;

import java.util.function.BiConsumer;
import javasabr.rlib.collections.dictionary.impl.ImmutableHashBasedRefToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.SimpleRefToRefEntry;

public interface RefToRefDictionary<K, V> extends Dictionary<K, V> {

  static <K, V> RefToRefDictionaryBuilder<K, V> builder() {
    return new RefToRefDictionaryBuilder<>();
  }

  static <K, V> RefToRefDictionaryBuilder<K, V> builder(
      Class<? super K> keyType, 
      Class<? super V> valueType) {
    return new RefToRefDictionaryBuilder<>();
  }

  static <K, V> RefToRefDictionaryBuilder<K, V> startWith(K key, V value) {
    return new RefToRefDictionaryBuilder<K, V>()
        .put(key, value);
  }
  
  static <K, V> RefToRefEntry<K, V> entry(K key, V value) {
    return new SimpleRefToRefEntry<>(key, value);
  }

  static <K, V> RefToRefDictionary<K, V> empty() {
    return ImmutableHashBasedRefToRefDictionary.empty();
  }

  static <K, V> RefToRefDictionary<K, V> of(K key, V value) {
    return ofEntries(entry(key, value));
  }

  static <K, V> RefToRefDictionary<K, V> of(K k1, V v1, K k2, V v2) {
    return ofEntries(entry(k1, v1), entry(k2, v2));
  }

  static <K, V> RefToRefDictionary<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
    return ofEntries(entry(k1, v1), entry(k2, v2), entry(k3, v3));
  }

  static <K, V> RefToRefDictionary<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    return ofEntries(entry(k1, v1), entry(k2, v2), entry(k3, v3), entry(k4, v4));
  }

  @SafeVarargs
  static <K, V> RefToRefDictionary<K, V> ofEntries(RefToRefEntry<K, V>... entries) {
    MutableRefToRefDictionary<K, V> mutable = DictionaryFactory.mutableRefToRefDictionary();
    for (var entry : entries) {
      mutable.put(entry.key(), entry.value());
    }
    return mutable.toReadOnly();
  }

  void forEach(BiConsumer<K, V> consumer);
}
