package javasabr.rlib.common.util.dictionary;

import java.util.Optional;
import javasabr.rlib.common.function.FourObjectConsumer;
import javasabr.rlib.common.function.NotNullBiConsumer;
import javasabr.rlib.common.function.NotNullBiFunction;
import javasabr.rlib.common.function.NotNullFunction;
import javasabr.rlib.common.function.NotNullSupplier;
import javasabr.rlib.common.function.NotNullTripleConsumer;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayFactory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The interface for implementing a key-value dictionary which using an object key.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
public interface ObjectDictionary<K, V> extends Dictionary<K, V> {

  /**
   * Create a new object dictionary for the key's type and value's type.
   *
   * @param keyValueType the key's and value's type.
   * @param <T> the key's and value's type.
   * @return the new object dictionary.
   */
  static <T> ObjectDictionary<T, T> ofType(Class<? super T> keyValueType) {
    return DictionaryFactory.newObjectDictionary();
  }

  /**
   * Create a new object dictionary for the key's type and value's type.
   *
   * @param keyType the key's type.
   * @param valueType the value's type.
   * @param <K> the key's type.
   * @param <V> the value's type.
   * @return the new object dictionary.
   */
  static <K, V> ObjectDictionary<K, V> ofType(
      Class<? super K> keyType,
      Class<? super V> valueType) {
    return DictionaryFactory.newObjectDictionary();
  }

  /**
   * Create a new object dictionary for the values.
   *
   * @param values the key-value values.
   * @param <K> the key's type.
   * @param <V> the value's type.
   * @return the new object dictionary.
   */
  static <K, V> ObjectDictionary<K, V> of(Object... values) {
    return new ReadOnlyFastObjectDictionary<>(values);
  }

  /**
   * Get an empty read-only object dictionary.
   *
   * @param <K> the key's type.
   * @param <V> the value's type.
   * @return the read-only empty dictionary.
   */
  static <K, V> ObjectDictionary<K, V> empty() {
    return ClassUtils.unsafeNNCast(DictionaryFactory.EMPTY_OD);
  }

  static <K, V, M extends ObjectDictionary<K, V>> M append(M first, M second) {
    second.copyTo(first);
    return first;
  }

  /**
   * Return true if this dictionary contains a mapping for the specified key.
   *
   * @param key key whose presence in this dictionary is to be tested.
   * @return true if this dictionary contains a mapping for the specified key.
   */
  default boolean containsKey(K key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Return the value to which the specified key is mapped, or {@code null} if this dictionary contains no mapping for
   * the key.
   *
   * @param key the key whose associated value is to be returned.
   * @return the value to which the specified key is mapped, or {@code null} if this dictionary contains no mapping for
   * the key.
   */
  default @Nullable V get(K key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Return the value to which the specified key is mapped, or default if this dictionary contains no mapping for the
   * key.
   *
   * @param key the key whose associated value is to be returned.
   * @param def the default value if no value in this dictionary.
   * @return the value to which the specified key is mapped, or default if this dictionary contains no mapping for the
   * key.
   */
  default @Nullable V getOrDefault(K key, V def) {
    var value = get(key);
    return value == null ? def : value;
  }

  /**
   * Return the optional value to which the specified key is mapped, or {@code null} if this dictionary contains no
   * mapping for the key.
   *
   * @param key the key whose associated value is to be returned.
   * @return the optional value to which the specified key is mapped.
   */
  default Optional<V> getOptional(K key) {
    return Optional.ofNullable(get(key));
  }

  /**
   * Get the value for the key. If the value doesn't exists, the factory will create new value, puts this value to this
   * dictionary and return this value.
   *
   * @param key the key.
   * @param factory the factory.
   * @return the stored value by the key or the new value.
   */
  default V getOrCompute(K key, NotNullSupplier<V> factory) {
    throw new UnsupportedOperationException();
  }

  /**
   * Get the value for the key. If the value doesn't exists, the factory will create new value, puts this value to this
   * dictionary and return this value.
   *
   * @param key the key.
   * @param factory the factory.
   * @return the stored value by the key or the new value.
   */
  default V getOrCompute(K key, NotNullFunction<K, V> factory) {
    throw new UnsupportedOperationException();
  }

  /**
   * Get the value for the key. If the value doesn't exists, the factory will create new value, puts this value to this
   * dictionary and return this value.
   *
   * @param <T> the argument's type.
   * @param key the key.
   * @param argument the additional argument.
   * @param factory the factory.
   * @return the stored value by the key or the new value.
   */
  default <T> V getOrCompute(K key, T argument, NotNullFunction<T, V> factory) {
    throw new UnsupportedOperationException();
  }

  /**
   * Get the value for the key. If the value doesn't exists, the factory will create new value, puts this value to this
   * dictionary and return this value.
   *
   * @param <T> the argument's type.
   * @param key the key.
   * @param argument the additional argument.
   * @param factory the factory.
   * @return the stored value by the key or the new value.
   * @see #getOrCompute(Object, Object, NotNullBiFunction)
   */
  default <T> V get(K key, T argument, NotNullBiFunction<K, T, V> factory) {
    return getOrCompute(key, argument, factory);
  }

  /**
   * Get the value for the key. If the value doesn't exists, the factory will create new value, puts this value to this
   * dictionary and return this value.
   *
   * @param <T> the argument's type.
   * @param key the key.
   * @param argument the additional argument.
   * @param factory the factory.
   * @return the stored value by the key or the new value.
   */
  default <T> V getOrCompute(
      K key,
      T argument,
      NotNullBiFunction<K, T, V> factory) {
    throw new UnsupportedOperationException();
  }

  /**
   * Put to the array all keys of this dictionary.
   *
   * @param container the container.
   * @return the array with all keys.
   */
  default Array<K> keyArray(Array<K> container) {
    throw new UnsupportedOperationException();
  }

  /**
   * Create an array with all keys of this dictionary.
   *
   * @param type the key's type.
   * @return the array with all keys of this dictionary.
   */
  default Array<K> keyArray(Class<K> type) {
    return keyArray(ArrayFactory.newArray(type, size()));
  }

  /**
   * Put the value by the key.
   *
   * @param key the value's key.
   * @param value the value.
   * @return the previous value for the key or null.
   */
  default @Nullable V put(K key, V value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Put the value by the key.
   *
   * @param key the value's key.
   * @param value the value.
   * @return the optional value of the previous value for the key.
   */
  default Optional<V> putOptional(K key, V value) {
    return Optional.ofNullable(put(key, value));
  }

  /**
   * Remove a mapping by the key.
   *
   * @param key the key.
   * @return the previous value for the key or null.
   */
  default @Nullable V remove(K key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Remove a mapping by the key.
   *
   * @param key the key.
   * @return the optional value of the previous value for the key.
   */
  default Optional<V> removeOptional(K key) {
    return Optional.ofNullable(remove(key));
  }

  /**
   * Performs the given action for each key-value pair of this dictionary.
   *
   * @param consumer the consumer.
   */
  default void forEach(NotNullBiConsumer<? super K, ? super V> consumer) {
    throw new UnsupportedOperationException();
  }

  /**
   * Performs the given action for each key-value pair of this dictionary.
   *
   * @param argument the argument.
   * @param consumer the consumer.
   * @param <T> the argument's type.
   */
  default <T> void forEach(
      T argument,
      NotNullTripleConsumer<? super T, ? super K, ? super V> consumer) {
    throw new UnsupportedOperationException();
  }

  /**
   * Performs the given action for each key-value pair of this dictionary.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param consumer the consumer.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   */
  default <F, S> void forEach(
      F first,
      S second,
      FourObjectConsumer<? super F, ? super S, ? super K, ? super V> consumer) {
    throw new UnsupportedOperationException();
  }
}


