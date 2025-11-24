package javasabr.rlib.collections.dictionary;

import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedIntToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedLongToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.DefaultMutableHashBasedRefToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.StampedLockBasedHashBasedRefToRefDictionary;
import javasabr.rlib.collections.dictionary.impl.gc.optimized.GcOptimizedMutableHashBasedIntToRefDictionary;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DictionaryFactory {
  public static <K, V> MutableRefToRefDictionary<K, V> mutableRefToRefDictionary() {
    return new DefaultMutableHashBasedRefToRefDictionary<>();
  }

  public static <V> MutableIntToRefDictionary<V> mutableIntToRefDictionary() {
    return new DefaultMutableHashBasedIntToRefDictionary<>();
  }

  public static <V> MutableIntToRefDictionary<V> gcOptimizedIntToRefDictionary() {
    return new GcOptimizedMutableHashBasedIntToRefDictionary<>();
  }

  public static <V> MutableLongToRefDictionary<V> mutableLongToRefDictionary() {
    return new DefaultMutableHashBasedLongToRefDictionary<>();
  }

  public static <K, V> MutableRefToRefDictionary<K, V> mutableRefToRefDictionary(
      Class<? super K> keyType,
      Class<? super V> valueType) {
    return new DefaultMutableHashBasedRefToRefDictionary<>();
  }

  public static <K, V> LockableRefToRefDictionary<K, V> stampedLockBasedRefToRefDictionary() {
    return new StampedLockBasedHashBasedRefToRefDictionary<>();
  }

  public static <K, V> LockableRefToRefDictionary<K, V> stampedLockBasedRefToRefDictionary(
      Class<? super K> keyType,
      Class<? super V> valueType) {
    return new StampedLockBasedHashBasedRefToRefDictionary<>();
  }
}
