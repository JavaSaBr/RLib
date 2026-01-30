package javasabr.rlib.collections.dictionary;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedIntToRefDictionary;
import org.jspecify.annotations.Nullable;

public interface MutableIntToRefDictionary<V> extends IntToRefDictionary<V> {

  static <V> MutableIntToRefDictionary<V> ofTypes(Class<V> valueType) {
    return new DefaultMutableHashBasedIntToRefDictionary<>();
  }

  V getOrCompute(int key, Supplier<V> factory);

  V getOrCompute(int key, IntFunction<V> factory);

  <T> V getOrCompute(int key, T arg1, Function<T, V> factory);

  /**
   * @return the previous value for the key or null.
   */
  @Nullable
  V put(int key, V value);

  /**
   * @return the existing value if the key is already present, or null if the key was absent and the new mapping was added.
   */
  @Nullable
  V putIfAbsent(int key, V value);
  
  void putAll(IntToRefDictionary<? extends V> dictionary);

  MutableIntToRefDictionary<V> append(IntToRefDictionary<? extends V> dictionary);

  /**
   * @return the optional value of the previous value for the key.
   */
  Optional<V> putOptional(int key, V value);

  /**
   * @return the previous value for the key or null.
   */
  @Nullable
  V remove(int key);

  /**
   * @return true if the expectedValue was removed
   */
  boolean remove(int key, V expectedValue);
  
  /**
   * @return the optional value of the previous value for the key.
   */
  Optional<V> removeOptional(int key);

  void clear();

  IntToRefDictionary<V> toReadOnly();
}
