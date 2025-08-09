package javasabr.rlib.common.util.dictionary;

import javasabr.rlib.common.function.NotNullFunction;
import javasabr.rlib.common.util.array.Array;
import javasabr.rlib.common.util.array.ArrayFactory;
import javasabr.rlib.common.util.pools.Reusable;
import org.jspecify.annotations.NullMarked;

/**
 * The interface for implementing a key-value dictionary.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
@NullMarked
public interface Dictionary<K, V> extends Iterable<V>, Reusable {

  /**
   * Replace the all values using the function.
   *
   * @param function the function.
   */
  default void apply(NotNullFunction<? super V, V> function) {
    throw new UnsupportedOperationException();
  }

  /**
   * Clear this dictionary.
   */
  default void clear() {
    throw new UnsupportedOperationException();
  }

  /**
   * Return true if this dictionary contains the value.
   *
   * @param value the value.
   * @return true if this dictionary contains the value.
   */
  default boolean containsValue(V value) {
    throw new UnsupportedOperationException();
  }

  @Override
  default void free() {
    clear();
  }

  /**
   * Returns true if this dictionary contains no key-value mappings.
   *
   * @return true if this dictionary contains no key-value mappings
   */
  default boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Put all data from this dictionary to the dictionary.
   *
   * @param dictionary the dictionary.
   */
  void copyTo(Dictionary<? super K, ? super V> dictionary);

  /**
   * Put all data from the dictionary to this dictionary.
   *
   * @param dictionary the dictionary with new data.
   */
  default void put(Dictionary<K, V> dictionary) {
    dictionary.copyTo(this);
  }

  /**
   * Return the number of key-value mappings in this dictionary.
   *
   * @return the number of key-value mappings in this dictionary.
   */
  default int size() {
    throw new UnsupportedOperationException();
  }

  /**
   * Collect all values from this dictionary.
   *
   * @param container the container to store the values.
   * @return the container with all values from this dictionary.
   */
  default Array<V> values(Array<V> container) {
    throw new UnsupportedOperationException();
  }

  /**
   * Collect all values from this dictionary.
   *
   * @param type the value's type.
   * @return the array with all values from this dictionary.
   */
  default Array<V> values(Class<V> type) {
    return values(ArrayFactory.newArray(type, size()));
  }
}
