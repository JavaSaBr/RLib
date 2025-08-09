package javasabr.rlib.common.util.dictionary;

import javasabr.rlib.common.util.pools.Reusable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Entry<T, V> extends Reusable {

  /**
   * Get the next entry.
   *
   * @return the next entry.
   */
  @Nullable
  T getNext();

  /**
   * Set the next entry.
   *
   * @param next the next entry.
   */
  void setNext(@Nullable T next);

  /**
   * Get the value.
   *
   * @return the value.
   */
  V getValue();

  /**
   * Set the value.
   *
   * @param value the new value of this entry.
   * @return the old value of null.
   */
  V setValue(V value);

  /**
   * Get the hash.
   *
   * @return the hash.
   */
  int getHash();
}
