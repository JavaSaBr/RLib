package javasabr.rlib.collections.array;

import java.io.Serializable;
import java.util.Arrays;
import java.util.RandomAccess;
import java.util.stream.IntStream;
import javasabr.rlib.collections.array.impl.ImmutableIntArray;

/**
 * An immutable array interface for primitive int values.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface IntArray extends Iterable<Integer>, Serializable, Cloneable, RandomAccess {

  ImmutableIntArray EMPTY = new ImmutableIntArray();

  /**
   * Returns an empty immutable int array.
   *
   * @return an empty int array
   * @since 10.0.0
   */
  static IntArray empty() {
    return EMPTY;
  }

  /**
   * Creates an immutable int array containing a single value.
   *
   * @param e1 the value
   * @return an immutable int array
   * @since 10.0.0
   */
  static IntArray of(int e1) {
    return new ImmutableIntArray(e1);
  }

  /**
   * Creates an immutable int array containing two values.
   *
   * @param e1 the first value
   * @param e2 the second value
   * @return an immutable int array
   * @since 10.0.0
   */
  static IntArray of(int e1, int e2) {
    return new ImmutableIntArray(e1, e2);
  }

  /**
   * Creates an immutable int array containing three values.
   *
   * @param e1 the first value
   * @param e2 the second value
   * @param e3 the third value
   * @return an immutable int array
   * @since 10.0.0
   */
  static IntArray of(int e1, int e2, int e3) {
    return new ImmutableIntArray(e1, e2, e3);
  }

  /**
   * Creates an immutable int array containing four values.
   *
   * @param e1 the first value
   * @param e2 the second value
   * @param e3 the third value
   * @param e4 the fourth value
   * @return an immutable int array
   * @since 10.0.0
   */
  static IntArray of(int e1, int e2, int e3, int e4) {
    return new ImmutableIntArray(e1, e2, e3, e4);
  }

  /**
   * Creates an immutable int array containing the specified values.
   *
   * @param elements the values to include
   * @return an immutable int array
   * @since 10.0.0
   */
  static IntArray of(int... elements) {
    return new ImmutableIntArray(elements);
  }

  /**
   * Creates an immutable copy of the specified int array.
   *
   * @param intArray the array to copy
   * @return an immutable copy
   * @since 10.0.0
   */
  static IntArray copyOf(IntArray intArray) {
    return new ImmutableIntArray(intArray.toArray());
  }

  /**
   * Creates an immutable int array with the same value repeated.
   *
   * @param value the value to repeat
   * @param count the number of times to repeat
   * @return an immutable int array with repeated values
   * @since 10.0.0
   */
  static IntArray repeated(int value, int count) {
    int[] values = new int[count];
    Arrays.fill(values, value);
    return new ImmutableIntArray(values);
  }

  /**
   * Returns the number of elements in this array.
   *
   * @return the number of elements
   * @since 10.0.0
   */
  int size();

  /**
   * Returns whether this array contains the specified value.
   *
   * @param value the value to search for
   * @return true if the array contains the value
   * @since 10.0.0
   */
  boolean contains(int value);

  /**
   * Returns whether this array contains all values from the specified array.
   *
   * @param array the array of values to check
   * @return true if all values are contained
   * @since 10.0.0
   */
  boolean containsAll(IntArray array);

  /**
   * Returns whether this array contains all values from the specified array.
   *
   * @param array the array of values to check
   * @return true if all values are contained
   * @since 10.0.0
   */
  boolean containsAll(int[] array);

  /**
   * Returns the first element.
   *
   * @return the first element
   * @throws java.util.NoSuchElementException if the array is empty
   * @since 10.0.0
   */
  int first();

  /**
   * Returns the element at the specified index.
   *
   * @param index the index of the element
   * @return the element at the index
   * @since 10.0.0
   */
  int get(int index);

  /**
   * Returns the last element.
   *
   * @return the last element
   * @throws java.util.NoSuchElementException if the array is empty
   * @since 10.0.0
   */
  int last();

  /**
   * Returns the index of the first occurrence of the specified value.
   *
   * @param value the value to search for
   * @return the index of the value or -1 if not found
   * @since 10.0.0
   */
  int indexOf(int value);

  /**
   * Returns the index of the last occurrence of the specified value.
   *
   * @param value the value to search for
   * @return the index of the value or -1 if not found
   * @since 10.0.0
   */
  int lastIndexOf(int value);

  /**
   * Returns whether this array is empty.
   *
   * @return true if empty
   * @since 10.0.0
   */
  boolean isEmpty();

  /**
   * Returns a new primitive int array containing all elements.
   *
   * @return an int array
   * @since 10.0.0
   */
  int[] toArray();

  /**
   * Returns a sequential stream of int values.
   *
   * @return an IntStream
   * @since 10.0.0
   */
  IntStream stream();

  /**
   * Returns an unsafe view providing direct access to internals.
   *
   * @return an unsafe view
   * @since 10.0.0
   */
  UnsafeIntArray asUnsafe();
}
