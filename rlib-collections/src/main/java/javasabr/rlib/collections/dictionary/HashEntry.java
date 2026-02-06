package javasabr.rlib.collections.dictionary;

/**
 * An entry that stores its hash code for hash-based data structures.
 *
 * @since 10.0.0
 */
public interface HashEntry {

  /**
   * Returns the hash code of this entry.
   *
   * @return the hash code
   * @since 10.0.0
   */
  int hash();
}
