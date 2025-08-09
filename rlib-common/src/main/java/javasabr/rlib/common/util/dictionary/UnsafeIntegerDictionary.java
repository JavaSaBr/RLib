package javasabr.rlib.common.util.dictionary;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The interface for implementing Unsafe part of {@link IntegerDictionary} API.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
@NullMarked
public interface UnsafeIntegerDictionary<V> extends IntegerDictionary<V> {

  /**
   * Get an array of all entries in this dictionary.
   *
   * @return the array of entries.
   */
  IntegerEntry<V>[] entries();

  /**
   * Remove an entry for the key.
   *
   * @param key the key of the entry.
   * @return removed entry.
   */
  @Nullable
  IntegerEntry<V> removeEntryForKey(final int key);
}
