package javasabr.rlib.collections.dictionary;

/**
 * An entry that maps a reference key to a reference value.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @since 10.0.0
 */
public interface RefToRefEntry<K, V> extends RefEntry<V> {

  /**
   * Returns the key of this entry.
   *
   * @return the key
   * @since 10.0.0
   */
  K key();

  /**
   * Sets the key of this entry.
   *
   * @param key the new key
   * @since 10.0.0
   */
  void key(K key);
}
