package javasabr.rlib.collections.array;

/**
 * An unsafe mutable view of an array providing direct modification operations.
 * Use with caution as modifications may violate array invariants.
 *
 * @param <E> the type of elements in this array
 * @since 10.0.0
 */
public interface UnsafeMutableArray<E> extends UnsafeArray<E>, MutableArray<E> {

  /**
   * Prepares internal storage for the expected size to avoid reallocations.
   *
   * @param expectedSize the expected number of elements
   * @return this for method chaining
   * @since 10.0.0
   */
  UnsafeMutableArray<E> prepareForSize(int expectedSize);

  /**
   * Adds an element without triggering copy-on-write or other safety mechanisms.
   *
   * @param element the element to add
   * @return this for method chaining
   * @since 10.0.0
   */
  UnsafeMutableArray<E> unsafeAdd(E element);

  /**
   * Removes the element at the specified index without bounds checking.
   *
   * @param index the index of the element to remove
   * @return the removed element
   * @since 10.0.0
   */
  E unsafeRemove(int index);

  /**
   * Sets the element at the specified index without bounds checking.
   *
   * @param index the index of the element to set
   * @param element the new element
   * @return this for method chaining
   * @since 10.0.0
   */
  UnsafeMutableArray<E> unsafeSet(int index, E element);

  /**
   * Trims the internal storage to match the current size.
   *
   * @return this for method chaining
   * @since 10.0.0
   */
  UnsafeMutableArray<E> trimToSize();

  /**
   * Attempts to trim the internal storage to the specified size.
   *
   * If the requested size is greater than the current capacity or less than the
   * current size, the array is not modified.
   *
   * @param internalStorageSize the desired internal storage size
   * @return this for method chaining
   * @since 10.0.alpha14
   */
  UnsafeMutableArray<E> tryTrimTo(int internalStorageSize);
}
