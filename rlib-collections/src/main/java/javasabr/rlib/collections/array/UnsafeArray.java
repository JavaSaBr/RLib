package javasabr.rlib.collections.array;

import org.jspecify.annotations.Nullable;

/**
 * An unsafe view of an array providing direct access to internal data structures.
 * Use with caution as modifications may violate array invariants.
 *
 * @param <E> the type of elements in this array
 * @since 10.0.0
 */
public interface UnsafeArray<E> extends Array<E> {

  /**
   * Returns the internal backing array.
   *
   * @return the backing array or null
   * @since 10.0.0
   */
  @Nullable E[] wrapped();

  /**
   * Returns the element at the specified index without bounds checking.
   *
   * @param index the index of the element
   * @return the element at the index
   * @since 10.0.0
   */
  E unsafeGet(int index);
}
