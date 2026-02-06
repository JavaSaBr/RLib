package javasabr.rlib.collections.dictionary;

/**
 * An entry that maps an int key to a reference value.
 *
 * @param <V> the type of the value
 * @since 10.0.0
 */
public interface IntToRefEntry<V> extends RefEntry<V> {

  /**
   * Returns the int key of this entry.
   *
   * @return the key
   * @since 10.0.0
   */
  int key();

  /**
   * Sets the int key of this entry.
   *
   * @param key the new key
   * @since 10.0.0
   */
  void key(int key);
}
