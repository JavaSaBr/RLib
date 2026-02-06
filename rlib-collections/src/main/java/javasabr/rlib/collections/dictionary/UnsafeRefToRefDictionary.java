package javasabr.rlib.collections.dictionary;

import org.jspecify.annotations.Nullable;

/**
 * An unsafe view of a reference-to-reference dictionary providing direct access to internal entries.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @param <E> the type of entries
 * @since 10.0.0
 */
public interface UnsafeRefToRefDictionary<K, V, E extends RefToRefEntry<K, V>>
    extends RefToRefDictionary<K, V> {

  /**
   * Returns the internal entry array.
   *
   * @return the entry array or null
   * @since 10.0.0
   */
  @Nullable E[] entries();
}
