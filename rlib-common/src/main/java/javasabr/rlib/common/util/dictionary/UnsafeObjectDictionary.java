package javasabr.rlib.common.util.dictionary;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The interface for implementing Unsafe part of {@link ObjectDictionary} API.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public interface UnsafeObjectDictionary<K, V> extends ObjectDictionary<K, V> {

  /**
   * Get an array of all entries in this dictionary.
   *
   * @return the array of entries.
   */
  ObjectEntry<K, V>[] entries();

  /**
   * Remove an entry for the key.
   *
   * @param key the key of the entry.
   * @return removed entry.
   */
  @Nullable ObjectEntry<K, V> removeEntryForKey(K key);
}
