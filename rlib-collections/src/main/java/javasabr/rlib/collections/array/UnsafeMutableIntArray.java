package javasabr.rlib.collections.array;

/**
 * An unsafe mutable view of an int array providing direct modification operations.
 * Use with caution as modifications may violate array invariants.
 *
 * @since 10.0.0
 */
public interface UnsafeMutableIntArray extends UnsafeIntArray, MutableIntArray {

  /**
   * Prepares internal storage for the expected size to avoid reallocations.
   *
   * @param expectedSize the expected number of elements
   * @return this for method chaining
   * @since 10.0.0
   */
  UnsafeMutableIntArray prepareForSize(int expectedSize);

  /**
   * Adds a value without triggering safety mechanisms.
   *
   * @param value the value to add
   * @return this for method chaining
   * @since 10.0.0
   */
  UnsafeMutableIntArray unsafeAdd(int value);

  /**
   * Removes the element at the specified index without bounds checking.
   *
   * @param index the index of the element to remove
   * @return the removed value
   * @since 10.0.0
   */
  int unsafeRemoveByInex(int index);

  /**
   * Sets the value at the specified index without bounds checking.
   *
   * @param index the index of the value to set
   * @param value the new value
   * @return this for method chaining
   * @since 10.0.0
   */
  UnsafeMutableIntArray unsafeSet(int index, int value);

  /**
   * Trims the internal storage to match the current size.
   *
   * @return this for method chaining
   * @since 10.0.0
   */
  UnsafeMutableIntArray trimToSize();
}
