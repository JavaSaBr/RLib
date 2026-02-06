package javasabr.rlib.collections.dictionary;

/**
 * An entry that maps a long key to a reference value.
 *
 * @param <V> the type of the value
 * @since 10.0.0
 */
public interface LongToRefEntry<V> extends RefEntry<V> {

  /**
   * Returns the long key of this entry.
   *
   * @return the key
   * @since 10.0.0
   */
  long key();

  /**
   * Sets the long key of this entry.
   *
   * @param key the new key
   * @since 10.0.0
   */
  void key(long key);
}
