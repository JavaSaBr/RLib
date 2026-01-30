package javasabr.rlib.collections.dictionary;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedRefToRefDictionary;
import org.jspecify.annotations.Nullable;

public interface MutableRefToRefDictionary<K, V> extends RefToRefDictionary<K, V> {

  static <K, V> MutableRefToRefDictionary<K, V> ofTypes(
      Class<K> keyType,
      Class<V> valueType) {
    return new DefaultMutableHashBasedRefToRefDictionary<>();
  }

  V getOrCompute(K key, Supplier<V> factory);

  V getOrCompute(K key, Function<K, V> factory);

  <T> V getOrCompute(K key, T arg1, Function<T, V> factory);

  /**
   * @return the previous value for the key or null.
   */
  @Nullable
  V put(K key, V value);

  /**
   * @return the existing value if the key is already present, or null if the key was absent and the new mapping was added.
   */
  @Nullable 
  V putIfAbsent(K key, V value);
  
  void putAll(RefToRefDictionary<? extends K, ? extends V> dictionary);

  MutableRefToRefDictionary<K, V> append(RefToRefDictionary<? extends K, ? extends V> dictionary);

  /**
   * @return the optional value of the previous value for the key.
   */
  Optional<V> putOptional(K key, V value);

  /**
   * @return the previous value for the key or null.
   */
  @Nullable
  V remove(K key);

  /**
   * @return true if the expectedValue was removed
   */
  boolean remove(K key, V expectedValue);
  
  /**
   * @return the optional value of the previous value for the key.
   */
  Optional<V> removeOptional(K key);

  void clear();

  RefToRefDictionary<K, V> toReadOnly();
}
