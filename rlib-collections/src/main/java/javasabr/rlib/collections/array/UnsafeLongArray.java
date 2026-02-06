package javasabr.rlib.collections.array;

/**
 * An unsafe view of a long array providing direct access to internal data structures.
 * Use with caution as modifications may violate array invariants.
 *
 * @since 10.0.0
 */
public interface UnsafeLongArray extends LongArray {

  /**
   * Returns the internal backing array.
   *
   * @return the backing array
   * @since 10.0.0
   */
  long[] wrapped();

  /**
   * Returns the element at the specified index without bounds checking.
   *
   * @param index the index of the element
   * @return the element at the index
   * @since 10.0.0
   */
  long unsafeGet(int index);
}
