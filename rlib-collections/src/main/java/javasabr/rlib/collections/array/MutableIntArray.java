package javasabr.rlib.collections.array;

/**
 * A mutable array interface for primitive int values.
 *
 * @since 10.0.0
 */
public interface MutableIntArray extends IntArray {

  /**
   * Adds a value to this array.
   *
   * @param value the value to add
   * @return true if this array changed as a result
   * @since 10.0.0
   */
  boolean add(int value);

  /**
   * Adds all values from the specified int array.
   *
   * @param array the array to add values from
   * @return true if this array changed as a result
   * @since 10.0.0
   */
  boolean addAll(IntArray array);

  /**
   * Adds all values from the specified primitive int array.
   *
   * @param array the array to add values from
   * @return true if this array changed as a result
   * @since 10.0.0
   */
  boolean addAll(int[] array);

  /**
   * Removes the element at the specified index.
   *
   * @param index the index of the element to remove
   * @return the element previously at the specified position
   * @since 10.0.0
   */
  int removeByIndex(int index);

  /**
   * Removes the first occurrence of the specified value.
   *
   * @param value the value to remove
   * @return true if this array contained the value
   * @since 10.0.0
   */
  boolean remove(int value);

  /**
   * Removes all values that are contained in the specified array.
   *
   * @param array the array of values to remove
   * @return true if this array changed as a result
   * @since 10.0.0
   */
  boolean removeAll(IntArray array);

  /**
   * Removes all values that are contained in the specified array.
   *
   * @param array the array of values to remove
   * @return true if this array changed as a result
   * @since 10.0.0
   */
  boolean removeAll(int[] array);

  /**
   * Replaces the value at the specified index.
   *
   * @param index the index of the value to replace
   * @param value the new value
   * @since 10.0.0
   */
  void replace(int index, int value);

  /**
   * Removes all values from this array.
   *
   * @since 10.0.0
   */
  void clear();

  /**
   * Returns an unsafe mutable view of this array.
   *
   * @return an unsafe mutable view
   * @since 10.0.0
   */
  @Override
  UnsafeMutableIntArray asUnsafe();
}
