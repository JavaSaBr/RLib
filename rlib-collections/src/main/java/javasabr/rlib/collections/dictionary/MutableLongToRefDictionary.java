package javasabr.rlib.collections.dictionary;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;
import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedLongToRefDictionary;
import org.jspecify.annotations.Nullable;

public interface MutableLongToRefDictionary<V> extends LongToRefDictionary<V> {

  static <V> MutableLongToRefDictionary<V> ofTypes(Class<V> valueType) {
    return new DefaultMutableHashBasedLongToRefDictionary<>();
  }

  V getOrCompute(long key, Supplier<V> factory);

  V getOrCompute(long key, LongFunction<V> factory);

  <T> V getOrCompute(long key, T arg1, Function<T, V> factory);

  /**
   * @return the previous value for the key or null.
   */
  @Nullable
  V put(long key, V value);

  /**
   * @return the existing value if the key is already present, or null if the key was absent and the new mapping was added.
   */
  @Nullable
  V putIfAbsent(long key, V value);
  
  void putAll(LongToRefDictionary<? extends V> dictionary);

  MutableLongToRefDictionary<V> append(LongToRefDictionary<? extends V> dictionary);

  /**
   * @return the optional value of the previous value for the key.
   */
  Optional<V> putOptional(long key, V value);

  /**
   * @return the previous value for the key or null.
   */
  @Nullable
  V remove(long key);

  /**
   * @return true if the expectedValue was removed
   */
  boolean remove(long key, V expectedValue);
  
  /**
   * @return the optional value of the previous value for the key.
   */
  Optional<V> removeOptional(long key);

  void clear();

  LongToRefDictionary<V> toReadOnly();
}
