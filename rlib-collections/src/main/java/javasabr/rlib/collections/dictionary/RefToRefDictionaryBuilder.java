package javasabr.rlib.collections.dictionary;

import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefToRefDictionaryBuilder<K, V> {
  
  MutableRefToRefDictionary<K, V> elements;

  public RefToRefDictionaryBuilder() {
    this.elements = DictionaryFactory.mutableRefToRefDictionary();
  }
  
  public RefToRefDictionaryBuilder<K, V> put(K key, V value) {
    this.elements.put(key, value);
    return this;
  }

  public RefToRefDictionaryBuilder<K, V> put(RefToRefDictionary<K, V> other) {
    this.elements.putAll(other);
    return this;
  }

  public RefToRefDictionaryBuilder<K, V> put(Map<K, V> other) {
    for (Map.Entry<K, V> entry : other.entrySet()) {
      elements.put(entry.getKey(), entry.getValue());
    }
    return this;
  }
  
  public RefToRefDictionary<K, V> build() {
    return elements.toReadOnly();
  }
}
