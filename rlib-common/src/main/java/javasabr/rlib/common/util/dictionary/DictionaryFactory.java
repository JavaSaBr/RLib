package javasabr.rlib.common.util.dictionary;

import org.jspecify.annotations.NullMarked;

/**
 * The factory for creating new {@link Dictionary}.
 *
 * @author JavaSaBr
 */
@NullMarked
public final class DictionaryFactory {

  public static final ObjectDictionary<?, ?> EMPTY_OD = new ReadOnlyFastObjectDictionary<>();
  public static final LongDictionary<?> EMPTY_LD = new ReadOnlyFastLongDictionary<>();

  public static <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary() {
    return new ConcurrentAtomicARSWLockIntegerDictionary<>();
  }

  public static <V> ConcurrentIntegerDictionary<V> newConcurrentAtomicIntegerDictionary(
      float loadFactor,
      int initCapacity) {
    return new ConcurrentAtomicARSWLockIntegerDictionary<>(loadFactor, initCapacity);
  }

  public static <V> ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary() {
    return new ConcurrentAtomicARSWLockLongDictionary<>();
  }

  public static <V> ConcurrentLongDictionary<V> newConcurrentAtomicLongDictionary(
      float loadFactor,
      int initCapacity) {
    return new ConcurrentAtomicARSWLockLongDictionary<>(loadFactor, initCapacity);
  }

  public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary() {
    return new ConcurrentAtomicARSWLockObjectDictionary<>();
  }

  public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentAtomicObjectDictionary(
      float loadFactor,
      int initCapacity) {
    return new ConcurrentAtomicARSWLockObjectDictionary<>(loadFactor, initCapacity);
  }

  public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentStampedLockObjectDictionary() {
    return new ConcurrentStampedLockObjectDictionary<>();
  }

  public static <K, V> ConcurrentObjectDictionary<K, V> newConcurrentStampedLockObjectDictionary(
      float loadFactor,
      int initCapacity) {
    return new ConcurrentStampedLockObjectDictionary<>(loadFactor, initCapacity);
  }

  public static <V> IntegerDictionary<V> newIntegerDictionary() {
    return new FastIntegerDictionary<>();
  }

  public static <V> IntegerDictionary<V> newIntegerDictionary(float loadFactor, int initCapacity) {
    return new FastIntegerDictionary<>(loadFactor, initCapacity);
  }

  public static <V> LongDictionary<V> newLongDictionary() {
    return new FastLongDictionary<>();
  }

  public static <V> LongDictionary<V> newLongDictionary(int initCapacity) {
    return new FastLongDictionary<>(initCapacity);
  }

  public static <V> LongDictionary<V> newLongDictionary(float loadFactor, int initCapacity) {
    return new FastLongDictionary<>(loadFactor, initCapacity);
  }

  public static <K, V> ObjectDictionary<K, V> newObjectDictionary() {
    return new FastObjectDictionary<>();
  }

  public static <K, V> ObjectDictionary<K, V> newObjectDictionary(float loadFactor, int initCapacity) {
    return new FastObjectDictionary<>(loadFactor, initCapacity);
  }

  public static <K, V> ObjectDictionary<K, V> newReadOnlyObjectDictionary(Object... values) {
    return new ReadOnlyFastObjectDictionary<>(values);
  }

  private DictionaryFactory() {
    throw new IllegalArgumentException();
  }
}
