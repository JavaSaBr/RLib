package javasabr.rlib.collections.dictionary;

import org.jspecify.annotations.Nullable;

/**
 * An entry that holds a reference value.
 *
 * @param <V> the type of the value
 * @since 10.0.0
 */
public interface RefEntry<V> {

  /**
   * Returns the value of this entry.
   *
   * @return the value or null
   * @since 10.0.0
   */
  @Nullable V value();

  /**
   * Sets the value of this entry.
   *
   * @param value the new value or null
   * @since 10.0.0
   */
  void value(@Nullable V value);
}
