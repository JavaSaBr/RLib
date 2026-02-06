
package javasabr.rlib.collections.array;

import java.io.Serializable;
import java.util.Arrays;
import java.util.RandomAccess;
import java.util.stream.LongStream;
import javasabr.rlib.collections.array.impl.ImmutableLongArray;

/**
 * An immutable array interface for primitive long values.
 *
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface LongArray extends Iterable<Long>, Serializable, Cloneable, RandomAccess {

  ImmutableLongArray EMPTY = new ImmutableLongArray();

  /**
   * Returns an empty immutable long array.
   *
   * @return an empty long array
   * @since 10.0.0
   */
  static LongArray empty() {
    return EMPTY;
  }

  /**
   * Creates an immutable long array containing a single value.
   *
   * @param e1 the value
   * @return an immutable long array
   * @since 10.0.0
   */
  static LongArray of(long e1) {
    return new ImmutableLongArray(e1);
  }

  /**
   * Creates an immutable long array containing two values.
   *
   * @param e1 the first value
   * @param e2 the second value
   * @return an immutable long array
   * @since 10.0.0
   */
  static LongArray of(long e1, long e2) {
    return new ImmutableLongArray(e1, e2);
  }

  /**
   * Creates an immutable long array containing three values.
   *
   * @param e1 the first value
   * @param e2 the second value
   * @param e3 the third value
   * @return an immutable long array
   * @since 10.0.0
   */
  static LongArray of(long e1, long e2, long e3) {
    return new ImmutableLongArray(e1, e2, e3);
  }

  /**
   * Creates an immutable long array containing four values.
   *
   * @param e1 the first value
   * @param e2 the second value
   * @param e3 the third value
   * @param e4 the fourth value
   * @return an immutable long array
   * @since 10.0.0
   */
  static LongArray of(long e1, long e2, long e3, long e4) {
    return new ImmutableLongArray(e1, e2, e3, e4);
  }

  /**
   * Creates an immutable long array containing the specified values.
   *
   * @param elements the values to include
   * @return an immutable long array
   * @since 10.0.0
   */
  static LongArray of(long... elements) {
    return new ImmutableLongArray(elements);
  }

  /**
   * Creates an immutable copy of the specified long array.
   *
   * @param intArray the array to copy
   * @return an immutable copy
   * @since 10.0.0
   */
  static LongArray copyOf(LongArray intArray) {
    return new ImmutableLongArray(intArray.toArray());
  }

  /**
   * Creates an immutable long array with the same value repeated.
   *
   * @param value the value to repeat
   * @param count the number of times to repeat
   * @return an immutable long array with repeated values
   * @since 10.0.0
   */
  static LongArray repeated(long value, int count) {
    long[] values = new long[count];
    Arrays.fill(values, value);
    return new ImmutableLongArray(values);
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
  boolean contains(long value);

  /**
   * Returns whether this array contains all values from the specified array.
   *
   * @param array the array of values to check
   * @return true if all values are contained
   * @since 10.0.0
   */
  boolean containsAll(LongArray array);

  /**
   * Returns whether this array contains all values from the specified array.
   *
   * @param array the array of values to check
   * @return true if all values are contained
   * @since 10.0.0
   */
  boolean containsAll(long[] array);

  /**
   * Returns the first element.
   *
   * @return the first element
   * @throws java.util.NoSuchElementException if the array is empty
   * @since 10.0.0
   */
  long first();

  /**
   * Returns the element at the specified index.
   *
   * @param index the index of the element
   * @return the element at the index
   * @since 10.0.0
   */
  long get(int index);

  /**
   * Returns the last element.
   *
   * @return the last element
   * @throws java.util.NoSuchElementException if the array is empty
   * @since 10.0.0
   */
  long last();

  /**
   * Returns the index of the first occurrence of the specified value.
   *
   * @param value the value to search for
   * @return the index of the value or -1 if not found
   * @since 10.0.0
   */
  int indexOf(long value);

  /**
   * Returns the index of the last occurrence of the specified value.
   *
   * @param value the value to search for
   * @return the index of the value or -1 if not found
   * @since 10.0.0
   */
  int lastIndexOf(long value);

  /**
   * Returns whether this array is empty.
   *
   * @return true if empty
   * @since 10.0.0
   */
  boolean isEmpty();

  /**
   * Returns a new primitive long array containing all elements.
   *
   * @return a long array
   * @since 10.0.0
   */
  long[] toArray();

  /**
   * Returns a sequential stream of long values.
   *
   * @return a LongStream
   * @since 10.0.0
   */
  LongStream stream();

  /**
   * Returns an unsafe view providing direct access to internals.
   *
   * @return an unsafe view
   * @since 10.0.0
   */
  UnsafeLongArray asUnsafe();
}
