package javasabr.rlib.common.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javasabr.rlib.functions.CharSupplier;
import javasabr.rlib.functions.DoubleObjConsumer;
import javasabr.rlib.functions.TriConsumer;
import javasabr.rlib.functions.TriFunction;
import javasabr.rlib.functions.TriPredicate;
import org.jspecify.annotations.Nullable;

/**
 * Utility class for array operations including creation, manipulation, searching, and conversion.
 *
 * @since 10.0.0
 */
public final class ArrayUtils {

  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  public static final Enum<?>[] EMPTY_ENUM_ARRAY = new Enum[0];
  public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

  public static final int[] EMPTY_INT_ARRAY = new int[0];
  public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  public static final short[] EMPTY_SHORT_ARRAY = new short[0];
  public static final long[] EMPTY_LONG_ARRAY = new long[0];
  public static final char[] EMPTY_CHAR_ARRAY = new char[0];

  /**
   * Creates an array from the given elements.
   *
   * @param <T> the element type.
   * @param elements the elements.
   * @return the array containing the elements.
   * @since 10.0.0
   */
  @SafeVarargs
  public static <T> T[] array(T... elements) {
    return elements;
  }

  /**
   * Converts an object integer array to primitive int array.
   *
   * @param origin the object integer array.
   * @return the primitive int array.
   * @since 9.2.1
   */
  public static int[] toIntArray(Integer[] origin) {
    if (origin.length < 1) {
      return ArrayUtils.EMPTY_INT_ARRAY;
    }
    var result = new int[origin.length];
    for (int i = 0; i < origin.length; i++) {
      result[i] = origin[i];
    }
    return result;
  }

  /**
   * Converts a string to primitive int array by splitting with regex.
   *
   * @param string the string.
   * @param regex the regex.
   * @return the primitive int array.
   * @throws NumberFormatException if some elements in the string are not an integer.
   * @since 9.2.1
   */
  public static int[] toIntArray(String string, String regex) {
    if (string.isBlank()) {
      return ArrayUtils.EMPTY_INT_ARRAY;
    }
    String[] elements = string.split(regex);
    if (elements.length < 1) {
      return ArrayUtils.EMPTY_INT_ARRAY;
    }
    var intArray = new int[elements.length];
    for (int i = 0; i < elements.length; i++) {
      intArray[i] = Integer.parseInt(elements[i].trim());
    }
    return intArray;
  }

  /**
   * Adds an element to the array and extends or creates the array if needed.
   *
   * @param <T> the element type.
   * @param array the array.
   * @param element the element.
   * @param type the type of array.
   * @return the result array with added element.
   * @since 10.0.0
   */
  public static <T> T[] addToArray(T @Nullable [] array, T element, Class<T> type) {
    if (array == null) {
      array = create(type, 1);
      array[0] = element;
      return array;
    }
    int length = array.length;
    array = copyOfAndExtend(array, 1);
    array[length] = element;
    return array;
  }

  /**
   * Clears all elements in the array by setting them to null.
   *
   * @param array the array.
   * @since 10.0.0
   */
  public static void clear(Object[] array) {
    Arrays.fill(array, null);
  }

  /**
   * Fills the array using the factory.
   *
   * @param <T> the element type.
   * @param array the array.
   * @param factory the element factory.
   * @since 10.0.0
   */
  public static <T> void fill(T[] array, Supplier<T> factory) {
    for (int i = 0; i < array.length; i++) {
      array[i] = factory.get();
    }
  }

  /**
   * Fills the char array using the factory.
   *
   * @param array the array.
   * @param factory the element factory.
   * @since 8.1.0
   */
  public static void fill(char[] array, CharSupplier factory) {
    for (int i = 0; i < array.length; i++) {
      array[i] = factory.getAsChar();
    }
  }

  /**
   * Fills the array using the factory which receives array index.
   *
   * @param <T> the element type.
   * @param array the array.
   * @param factory the element factory.
   * @since 10.0.0
   */
  public static <T> void fill(T[] array, IntFunction<T> factory) {
    Arrays.setAll(array, factory);
  }

  /**
   * Fills the array using the factory with an additional argument.
   *
   * @param <T> the element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the additional argument.
   * @param factory the element factory.
   * @since 10.0.0
   */
  public static <T, A> void fill(T[] array, A arg, Function<A, T> factory) {
    for (int i = 0; i < array.length; i++) {
      array[i] = factory.apply(arg);
    }
  }

  /**
   * Combines two int arrays into one.
   *
   * @param base the source array.
   * @param added the additional array.
   * @return the combined array.
   * @since 10.0.0
   */
  public static int[] combine(int @Nullable [] base, int @Nullable [] added) {
    if (base == null) {
      return added == null ? new int[0] : added;
    } else if (added == null || added.length < 1) {
      return base;
    }
    int[] result = new int[base.length + added.length];
    int index = 0;
    for (int val : base) {
      result[index++] = val;
    }
    for (int val : added) {
      result[index++] = val;
    }
    return result;
  }

  /**
   * Combines two arrays to one single array.
   *
   * @param <T> the base array component type.
   * @param <E> the added array component type.
   * @param base the base array.
   * @param added the additional array.
   * @return the combined array.
   * @since 10.0.0
   */
  public static <T, E extends T> T[] combine(T[] base, E @Nullable [] added) {
    return combine(base, added, resolveComponentType(base));
  }

  /**
   * Combines two arrays to one single array.
   *
   * @param <T> the base array component type.
   * @param <E> the added array component type.
   * @param base the base array.
   * @param added the additional array.
   * @param type the base array component type.
   * @return the combined array.
   * @since 10.0.0
   */
  public static <T, E extends T> T[] combine(T @Nullable [] base, E @Nullable [] added, Class<T> type) {
    if (base == null) {
      return added == null ? create(type, 0) : added;
    } else if (added == null || added.length < 1) {
      return base;
    }
    T[] result = create(type, base.length + added.length);
    int index = 0;
    for (T object : base) {
      result[index++] = object;
    }
    for (E object : added) {
      result[index++] = object;
    }
    return result;
  }

  /**
   * Combines two arrays to one single array with unique elements.
   *
   * @param <T> the base array component type.
   * @param <E> the added array component type.
   * @param base the base array.
   * @param added the additional array.
   * @return the combined array with unique elements.
   * @since 10.0.0
   */
  public static <T, E extends T> T[] combineUniq(T[] base, E @Nullable [] added) {
    return combineUniq(base, added, resolveComponentType(base));
  }

  /**
   * Combines two arrays to one single array with unique elements.
   *
   * @param <T> the base array component type.
   * @param <E> the added array component type.
   * @param base the base array.
   * @param added the additional array.
   * @param type the base array component type.
   * @return the combined array with unique elements.
   * @since 10.0.0
   */
  public static <T, E extends T> T[] combineUniq(T @Nullable [] base, E @Nullable [] added, Class<T> type) {
    if (base == null) {
      return added == null ? create(type, 0) : added;
    } else if (added == null || added.length < 1) {
      return base;
    }
    var result = new HashSet<T>(base.length + added.length);
    result.addAll(Arrays.asList(base));
    result.addAll(Arrays.asList(added));
    return result.toArray(create(type, result.size()));
  }

  /**
   * Checks if the array contains the value.
   *
   * @param array the array.
   * @param val the value.
   * @return true if the array contains the value.
   * @since 10.0.0
   */
  public static boolean contains(int[] array, int val) {
    for (int value : array) {
      if (value == val) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if an array contains an object.
   *
   * @param array the array.
   * @param object the object.
   * @return true if the array contains the object.
   * @since 10.0.0
   */
  public static boolean contains(Object[] array, Object object) {
    for (var element : array) {
      if (Objects.equals(element, object)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Copies and extends the byte array.
   *
   * @param old the source array.
   * @param added the added size.
   * @return the new array.
   * @since 10.0.0
   */
  public static byte[] copyOf(byte[] old, int added) {
    var copy = new byte[old.length + added];
    System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));
    return copy;
  }

  /**
   * Copies and extends the int array.
   *
   * @param old the source array.
   * @param added the added size.
   * @return the new array.
   * @since 10.0.0
   */
  public static int[] copyOf(int[] old, int added) {
    var copy = new int[old.length + added];
    System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));
    return copy;
  }

  /**
   * Copies and extends the long array.
   *
   * @param old the source array.
   * @param added the added size.
   * @return the new array.
   * @since 10.0.0
   */
  public static long[] copyOf(long[] old, int added) {
    var copy = new long[old.length + added];
    System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));
    return copy;
  }

  /**
   * Copies a native array.
   *
   * @param <T> the array component type.
   * @param original the original array.
   * @return the new copied native array.
   * @since 10.0.0
   */
  public static <T> T[] copyOf(T[] original) {
    return Arrays.copyOf(original, original.length);
  }

  /**
   * Copies an array and extends if needed.
   *
   * @param <T> the array component type.
   * @param original the original array.
   * @param added the additional size.
   * @return the new copied array.
   * @since 10.0.0
   */
  public static <T> T[] copyOfAndExtend(T[] original, int added) {
    return Arrays.copyOf(original, original.length + added);
  }

  /**
   * Copies and extends the array with offset.
   *
   * @param <T> the array component type.
   * @param original the source array.
   * @param offset the start position to copy in new array.
   * @param added the added size.
   * @return the new array.
   * @since 10.0.0
   */
  public static <T> T[] copyOf(T[] original, int offset, int added) {
    Class<? extends Object[]> newType = original.getClass();
    T[] newArray = create(newType.getComponentType(), original.length + added);
    System.arraycopy(original, 0, newArray, offset, Math.min(original.length, newArray.length));
    return newArray;
  }

  /**
   * Copies data from the source array to the destination array.
   *
   * @param source the source array.
   * @param target the target array.
   * @since 10.0.0
   */
  public static void copyTo(int[] source, int[] target) {
    System.arraycopy(source, 0, target, 0, source.length);
  }

  /**
   * Copies data from the source array to the destination array with offsets.
   *
   * @param source the source array.
   * @param target the target array.
   * @param sourceOffset the source offset.
   * @param targetOffset the target offset.
   * @param length the length of data.
   * @since 10.0.0
   */
  public static void copyTo(int[] source, int[] target, int sourceOffset, int targetOffset, int length) {
    System.arraycopy(source, sourceOffset, target, targetOffset, length);
  }

  /**
   * Copies a part of the int array to a new array.
   *
   * @param original the source array.
   * @param from the start element.
   * @param to the last element.
   * @return the new array.
   * @since 10.0.0
   */
  public static int[] copyOfRange(int[] original, int from, int to) {
    int newLength = to - from;
    var copy = new int[newLength];
    System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
    return copy;
  }

  /**
   * Copies a part of the long array to a new array.
   *
   * @param original the source array.
   * @param from the start element.
   * @param to the last element.
   * @return the new array.
   * @since 10.0.0
   */
  public static long[] copyOfRange(long[] original, int from, int to) {
    int newLength = to - from;
    var copy = new long[newLength];
    System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
    return copy;
  }

  /**
   * Copies a part of the array to a new array.
   *
   * @param <T> the array component type.
   * @param original the source array.
   * @param from the start element.
   * @param to the last element.
   * @return the new array.
   * @since 10.0.0
   */
  public static <T> T[] copyOfRange(T[] original, int from, int to) {
    return Arrays.copyOfRange(original, from, to);
  }

  /**
   * Gets the component type of an array.
   *
   * @param <T> the array component type.
   * @param example the array example.
   * @return the component type.
   * @since 10.0.0
   */
  public static <T> Class<T> resolveComponentType(T[] example) {
    return ClassUtils.unsafeNNCast(example
        .getClass()
        .getComponentType());
  }

  /**
   * Creates an array by the example array type.
   *
   * @param <T> the array component type.
   * @param example the array example.
   * @param size the size.
   * @return the new array.
   * @since 10.0.0
   */
  public static <T> T[] create(T[] example, int size) {
    Class<?> componentType = example
        .getClass()
        .getComponentType();
    return ClassUtils.unsafeCast(java.lang.reflect.Array.newInstance(componentType, size));
  }

  /**
   * Creates an array by the component type.
   *
   * @param <T> the array component type.
   * @param type the array type.
   * @param size the size.
   * @return the new array.
   * @since 10.0.0
   */
  public static <T> T[] create(Class<?> type, int size) {
    return ClassUtils.unsafeCast(java.lang.reflect.Array.newInstance(type, size));
  }

  /**
   * Finds an index of the object in the array.
   *
   * @param array the array.
   * @param object the object.
   * @return the object index or -1.
   * @since 10.0.0
   */
  public static int indexOf(Object[] array, @Nullable Object object) {
    int index = 0;
    for (var element : array) {
      if (Objects.equals(element, object)) {
        return index;
      }
      index++;
    }
    return -1;
  }

  /**
   * Sorts the comparable array.
   *
   * @param array the array.
   * @since 10.0.0
   */
  public static void sort(Comparable<?>[] array) {
    java.util.Arrays.sort(array);
  }

  /**
   * Sorts the int array.
   *
   * @param array the array.
   * @since 10.0.0
   */
  public static void sort(int[] array) {
    java.util.Arrays.sort(array);
  }

  /**
   * Sorts the int array in range.
   *
   * @param array the array.
   * @param fromIndex the start index.
   * @param toIndex the last index.
   * @since 10.0.0
   */
  public static void sort(int[] array, int fromIndex, int toIndex) {
    java.util.Arrays.sort(array, fromIndex, toIndex);
  }

  /**
   * Sorts the long array in range.
   *
   * @param array the array.
   * @param fromIndex the start index.
   * @param toIndex the last index.
   * @since 10.0.0
   */
  public static void sort(long[] array, int fromIndex, int toIndex) {
    java.util.Arrays.sort(array, fromIndex, toIndex);
  }

  /**
   * Sorts the array using comparator.
   *
   * @param <T> the element type.
   * @param array the array.
   * @param comparator the comparator.
   * @since 10.0.0
   */
  public static <T> void sort(T[] array, Comparator<? super T> comparator) {
    java.util.Arrays.sort(array, comparator);
  }

  /**
   * Sorts the array in range using comparator.
   *
   * @param <T> the element type.
   * @param array the array.
   * @param fromIndex the start index.
   * @param toIndex the last index.
   * @param comparator the comparator.
   * @since 10.0.0
   */
  public static <T> void sort(T[] array, int fromIndex, int toIndex, Comparator<? super T> comparator) {
    java.util.Arrays.sort(array, fromIndex, toIndex, comparator);
  }

  /**
   * Converts the array to a string presentation using custom function.
   *
   * @param <T> the element type.
   * @param array the array.
   * @param size the size.
   * @param toString the converter function.
   * @return the string presentation.
   * @since 10.0.0
   */
  public static <T> String toString(T[] array, int size, Function<T, String> toString) {
    if (array.length < 1) {
      return "[]";
    }
    var builder = new StringBuilder(20);
    builder.append('[');
    for (int i = 0, last = size - 1; i < size; i++) {
      builder.append(toString.apply(array[i]));
      if (i == last) {
        break;
      }
      builder.append(", ");
    }
    builder.append(']');
    return builder.toString();
  }

  /**
   * Converts the int array to a string presentation.
   *
   * @param array the array.
   * @return the string presentation.
   * @since 10.0.0
   */
  public static String toString(int @Nullable [] array) {
    return toString(array, ", ", true, true);
  }

  /**
   * Converts the int array to a string presentation.
   *
   * @param array the array.
   * @param separator the separator.
   * @param needType true if need adding type of array.
   * @param needBrackets true if need adding brackets.
   * @return the string presentation of the array.
   * @since 10.0.0
   */
  public static String toString(int @Nullable [] array, String separator, boolean needType, boolean needBrackets) {
    int length = array == null ? 0 : array.length;
    return toString(array, 0, length, separator, needType, needBrackets);
  }

  /**
   * Converts the int array to a string presentation with range.
   *
   * @param array the array.
   * @param offset the offset.
   * @param length the length.
   * @param separator the separator.
   * @param needType true if need adding type of array.
   * @param needBrackets true if need adding brackets.
   * @return the string presentation of the array.
   * @since 10.0.0
   */
  public static String toString(
      int @Nullable [] array,
      int offset,
      int length,
      String separator,
      boolean needType,
      boolean needBrackets) {
    if (array == null) {
      array = EMPTY_INT_ARRAY;
    }
    var builder = new StringBuilder();
    if (needType) {
      builder.append("int");
    }
    if (needBrackets) {
      builder.append('[');
    }
    for (int i = offset, limit = offset + length - 1; i <= limit; i++) {
      builder.append(array[i]);
      if (i == limit) {
        break;
      }
      builder.append(separator);
    }
    if (needBrackets) {
      builder.append(']');
    }
    return builder.toString();
  }

  /**
   * Converts the long array to a string presentation with range.
   *
   * @param array the array.
   * @param offset the offset.
   * @param length the length.
   * @param separator the separator.
   * @param needType true if need adding type of array.
   * @param needBrackets true if need adding brackets.
   * @return the string presentation of the array.
   * @since 10.0.0
   */
  public static String toString(
      long @Nullable [] array,
      int offset,
      int length,
      String separator,
      boolean needType,
      boolean needBrackets) {

    if (array == null) {
      array = EMPTY_LONG_ARRAY;
    }
    var builder = new StringBuilder();
    if (needType) {
      builder.append("long");
    }
    if (needBrackets) {
      builder.append('[');
    }
    for (int i = offset, limit = offset + length - 1; i <= limit; i++) {
      builder.append(array[i]);
      if (i == limit) {
        break;
      }
      builder.append(separator);
    }
    if (needBrackets) {
      builder.append(']');
    }
    return builder.toString();
  }

  /**
   * Converts the float array to a string presentation.
   *
   * @param array the array.
   * @return the string presentation.
   * @since 10.0.0
   */
  public static String toString(float @Nullable [] array) {
    return toString(array, ", ", true, true);
  }

  /**
   * Converts the float array to a string presentation.
   *
   * @param array the array.
   * @param separator the separator.
   * @param needType true if need adding type of array.
   * @param needBrackets true if need adding brackets.
   * @return the string presentation of the array.
   * @since 10.0.0
   */
  public static String toString(float @Nullable [] array, String separator, boolean needType, boolean needBrackets) {
    if (array == null) {
      array = EMPTY_FLOAT_ARRAY;
    }
    var builder = new StringBuilder();
    if (needType) {
      builder.append("float");
    }
    if (needBrackets) {
      builder.append('[');
    }
    for (int i = 0, length = array.length - 1; i <= length; i++) {
      builder.append(array[i]);
      if (i == length) {
        break;
      }
      builder.append(separator);
    }
    if (needBrackets) {
      builder.append(']');
    }
    return builder.toString();
  }

  /**
   * Converts the object array to a string presentation.
   *
   * @param array the array.
   * @return the string presentation of the array.
   * @since 10.0.0
   */
  public static String toString(Object @Nullable [] array) {
    return toString(array, ", ", true, true);
  }

  /**
   * Converts the object array to a string presentation.
   *
   * @param array the array.
   * @param separator the separator.
   * @param needType true if need adding type of array.
   * @param needBrackets true if need adding brackets.
   * @return the string presentation of the array.
   * @since 10.0.0
   */
  public static String toString(
      @Nullable Object @Nullable [] array,
      String separator,
      boolean needType,
      boolean needBrackets) {
    return toString(array, 0, array == null ? 0 : array.length, separator, needType, needBrackets);
  }

  /**
   * Converts the object array to a string presentation with range.
   *
   * @param array the array.
   * @param start the start.
   * @param length the length.
   * @param separator the separator.
   * @param needType true if need adding type of array.
   * @param needBrackets true if need adding brackets.
   * @return the string presentation of the array.
   * @since 10.0.0
   */
  public static String toString(
      @Nullable Object @Nullable [] array,
      int start,
      int length,
      String separator,
      boolean needType,
      boolean needBrackets) {
    if (array == null) {
      array = EMPTY_OBJECT_ARRAY;
    }
    var builder = new StringBuilder();
    if (needType) {
      builder.append(array
          .getClass()
          .getSimpleName());
    }
    if (needBrackets) {
      builder.append('[');
    }
    for (int i = start, limit = start + length - 1; i <= limit; i++) {
      builder.append(array[i]);
      if (i == limit) {
        break;
      }
      builder.append(separator);
    }
    if (needBrackets) {
      builder.append(']');
    }
    return builder.toString();
  }

  /**
   * Applies the function to each element of the array.
   *
   * @param <T> the element type.
   * @param array the array.
   * @param consumer the function.
   * @since 10.0.0
   */
  public static <T> void forEach(T @Nullable [] array, Consumer<T> consumer) {
    if (isNotEmpty(array)) {
      for (T element : array) {
        consumer.accept(element);
      }
    }
  }

  /**
   * Applies the function to each filtered element of the array.
   *
   * @param <T> the element type.
   * @param array the array.
   * @param condition the condition.
   * @param consumer the function.
   * @since 10.0.0
   */
  public static <T> void forEach(T @Nullable [] array, Predicate<T> condition, Consumer<T> consumer) {
    if (isNotEmpty(array)) {
      for (T element : array) {
        if (condition.test(element)) {
          consumer.accept(element);
        }
      }
    }
  }

  /**
   * Applies the consumer for each element of the double array.
   *
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the additional argument.
   * @param consumer the consumer.
   * @since 10.0.0
   */
  public static <A> void forEach(double @Nullable [] array, A arg, DoubleObjConsumer<A> consumer) {
    if (isNotEmpty(array)) {
      for (double element : array) {
        consumer.accept(element, arg);
      }
    }
  }

  /**
   * Applies the consumer for each element of the array.
   *
   * @param <T> the element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the additional argument.
   * @param consumer the consumer.
   * @since 10.0.0
   */
  public static <T, A> void forEach(T @Nullable [] array, A arg, BiConsumer<T, A> consumer) {
    if (isNotEmpty(array)) {
      for (T element : array) {
        consumer.accept(element, arg);
      }
    }

  }

  /**
   * Handles elements of the array using sub-element getter and final function.
   *
   * @param <T> the element type.
   * @param <A> the argument type.
   * @param <R> the sub-element type.
   * @param array the array.
   * @param arg the additional argument.
   * @param getter the function to get sub element.
   * @param consumer the final function.
   * @since 10.0.0
   */
  public static <T, A, R> void forEach(T @Nullable [] array, A arg, Function<T, R> getter, BiConsumer<R, A> consumer) {
    if (isNotEmpty(array)) {
      for (T element : array) {
        consumer.accept(getter.apply(element), arg);
      }
    }
  }

  /**
   * Applies the function to each filtered element of the array.
   *
   * @param <T> the element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the additional argument.
   * @param condition the condition.
   * @param consumer the function.
   * @since 10.0.0
   */
  public static <T, A> void forEach(T @Nullable [] array, A arg, Predicate<T> condition, BiConsumer<T, A> consumer) {
    if (isNotEmpty(array)) {
      for (T element : array) {
        if (condition.test(element)) {
          consumer.accept(element, arg);
        }
      }
    }
  }

  /**
   * Applies the function to each filtered element of the array using sub-element getter.
   *
   * @param <T> the element type.
   * @param <R> the sub-element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the additional argument.
   * @param condition the condition.
   * @param getter the function to get sub element.
   * @param consumer the final function.
   * @since 10.0.0
   */
  public static <T, R, A> void forEach(
      T @Nullable [] array,
      A arg,
      Predicate<T> condition,
      Function<T, R> getter,
      BiConsumer<R, A> consumer) {
    if (isNotEmpty(array)) {
      for (T element : array) {
        if (condition.test(element)) {
          consumer.accept(getter.apply(element), arg);
        }
      }
    }
  }

  /**
   * Applies the function to each element of the array with two arguments.
   *
   * @param <T> the element type.
   * @param <A> the first argument type.
   * @param <B> the second argument type.
   * @param array the array.
   * @param arg1 the first argument.
   * @param arg2 the second argument.
   * @param consumer the function.
   * @since 10.0.0
   */
  public static <T, A, B> void forEach(T @Nullable [] array, A arg1, B arg2, TriConsumer<T, A, B> consumer) {
    if (isNotEmpty(array)) {
      for (final T element : array) {
        consumer.accept(element, arg1, arg2);
      }
    }
  }

  /**
   * Applies the function to each sub-element of the array with two arguments.
   *
   * @param <T> the element type.
   * @param <R> the sub-element type.
   * @param <A> the first argument type.
   * @param <B> the second argument type.
   * @param array the array.
   * @param arg1 the first argument.
   * @param arg2 the second argument.
   * @param getter the function to get sub element.
   * @param consumer the function.
   * @since 10.0.0
   */
  public static <T, R, A, B> void forEach(
      T @Nullable [] array,
      A arg1,
      B arg2,
      TriFunction<T, A, B, R> getter,
      TriConsumer<R, A, B> consumer) {
    if (isNotEmpty(array)) {
      for (T element : array) {
        R subElement = getter.apply(element, arg1, arg2);
        consumer.accept(subElement, arg1, arg2);
      }
    }

  }

  /**
   * Finds an index of the element in the array using condition.
   *
   * @param <T> the element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the argument.
   * @param condition the condition.
   * @return the index of the element or -1.
   * @since 10.0.0
   */
  public static <T, A> int indexOf(T @Nullable [] array, A arg, BiPredicate<T, A> condition) {
    return indexOf(array, arg, condition, 0, array == null ? 0 : array.length);
  }

  /**
   * Finds an index of the element in an array using condition in the range.
   *
   * @param <T> the element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the argument.
   * @param condition the condition.
   * @param startIndex the start index.
   * @param endIndex the end index.
   * @return the index of the element or -1.
   * @since 10.0.0
   */
  public static <T, A> int indexOf(
      T @Nullable [] array,
      A arg,
      BiPredicate<T, A> condition,
      int startIndex,
      int endIndex) {
    if (isNotEmpty(array)) {
      for (int i = startIndex; i < endIndex; i++) {
        if (condition.test(array[i], arg)) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Calculates a count of interesting elements of the array.
   *
   * @param <T> the element type.
   * @param array the array.
   * @param condition the condition.
   * @return the count of elements.
   * @since 10.0.0
   */
  public static <T> int count(T @Nullable [] array, Predicate<T> condition) {
    if (isEmpty(array)) {
      return 0;
    }
    int count = 0;
    for (T element : array) {
      if (condition.test(element)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Finds an element in the array using the condition.
   *
   * @param <T> the array element type.
   * @param array the array.
   * @param condition the condition.
   * @return the element or null.
   * @since 10.0.0
   */
  @Nullable
  public static <T> T findAny(T @Nullable [] array, Predicate<? super T> condition) {
    if (isNotEmpty(array)) {
      for (T element : array) {
        if (condition.test(element)) {
          return element;
        }
      }
    }
    return null;
  }

  /**
   * Checks if there is at least an element for the condition.
   *
   * @param <T> the array element type.
   * @param array the array.
   * @param condition the condition.
   * @return true if there is at least an element for the condition.
   * @since 10.0.0
   */
  public static <T> boolean anyMatch(T @Nullable [] array, Predicate<? super T> condition) {
    return findAny(array, condition) != null;
  }

  /**
   * Finds an element in the array using the condition.
   *
   * @param <T> the array element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the argument.
   * @param condition the condition.
   * @return the element or null.
   * @since 10.0.0
   */
  @Nullable
  public static <T, A> T findAny(T @Nullable [] array, A arg, BiPredicate<? super T, A> condition) {
    if (isEmpty(array)) {
      return null;
    }
    for (T element : array) {
      if (condition.test(element, arg)) {
        return element;
      }
    }
    return null;
  }

  /**
   * Checks if there is at least an element for the condition.
   *
   * @param <T> the array element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the argument.
   * @param condition the condition.
   * @return true if there is at least an element for the condition.
   * @since 10.0.0
   */
  public static <T, A> boolean anyMatch(T @Nullable [] array, A arg, BiPredicate<? super T, A> condition) {
    return findAny(array, arg, condition) != null;
  }

  /**
   * Finds a sub-element in the array using the function to get a sub-element and the condition.
   *
   * @param <T> the element type.
   * @param <R> the sub-element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the argument.
   * @param getter the function to get a sub-element.
   * @param condition the condition.
   * @return the element or null.
   * @since 10.0.0
   */
  @Nullable
  public static <T, R, A> R findAny(T @Nullable [] array, A arg, Function<T, R> getter, BiPredicate<R, A> condition) {
    if (isEmpty(array)) {
      return null;
    }
    for (T element : array) {
      R subElement = getter.apply(element);
      if (condition.test(subElement, arg)) {
        return subElement;
      }
    }
    return null;
  }

  /**
   * Finds a sub-element in the array using the function to get a sub-element and conditions.
   *
   * @param <T> the element type.
   * @param <R> the sub-element type.
   * @param <A> the argument type.
   * @param array the array.
   * @param arg the argument.
   * @param condition the condition.
   * @param getter the function to get a sub-element.
   * @param secondCondition the second condition.
   * @return the element or null.
   * @since 10.0.0
   */
  @Nullable
  public static <T, R, A> R findAny(
      T @Nullable [] array,
      A arg,
      Predicate<T> condition,
      Function<T, R> getter,
      BiPredicate<R, A> secondCondition) {
    if (isEmpty(array)) {
      return null;
    }
    for (T element : array) {
      if (!condition.test(element)) {
        continue;
      }
      R subElement = getter.apply(element);
      if (secondCondition.test(subElement, arg)) {
        return subElement;
      }
    }
    return null;
  }

  /**
   * Finds an element in the array using the condition with two arguments.
   *
   * @param <T> the element type.
   * @param <F> the first argument type.
   * @param <S> the second argument type.
   * @param array the array.
   * @param arg1 the first argument.
   * @param arg2 the second argument.
   * @param condition the condition.
   * @return the element or null.
   * @since 10.0.0
   */
  @Nullable
  public static <T, F, S> T findAny(T @Nullable [] array, F arg1, S arg2, TriPredicate<T, F, S> condition) {
    if (isNotEmpty(array)) {
      for (T element : array) {
        if (condition.test(element, arg1, arg2)) {
          return element;
        }
      }
    }
    return null;
  }

  /**
   * Checks if the byte array is not null or empty.
   *
   * @param array the array.
   * @return true if the array is not null or empty.
   * @since 10.0.0
   */
  public static boolean isNotEmpty(byte @Nullable [] array) {
    return array != null && array.length > 0;
  }

  /**
   * Checks if the byte array is null or empty.
   *
   * @param array the array.
   * @return true if the array is null or empty.
   * @since 10.0.0
   */
  public static boolean isEmpty(byte @Nullable [] array) {
    return array == null || array.length == 0;
  }

  /**
   * Checks if the short array is not null or empty.
   *
   * @param array the array.
   * @return true if the array is not null or empty.
   * @since 10.0.0
   */
  public static boolean isNotEmpty(short @Nullable [] array) {
    return array != null && array.length > 0;
  }

  /**
   * Checks if the short array is null or empty.
   *
   * @param array the array.
   * @return true if the array is null or empty.
   * @since 10.0.0
   */
  public static boolean isEmpty(short @Nullable [] array) {
    return array == null || array.length == 0;
  }

  /**
   * Checks if the char array is not null or empty.
   *
   * @param array the array.
   * @return true if the array is not null or empty.
   * @since 10.0.0
   */
  public static boolean isNotEmpty(char @Nullable [] array) {
    return array != null && array.length > 0;
  }

  /**
   * Checks if the char array is null or empty.
   *
   * @param array the array.
   * @return true if the array is null or empty.
   * @since 10.0.0
   */
  public static boolean isEmpty(char @Nullable [] array) {
    return array == null || array.length == 0;
  }

  /**
   * Checks if the int array is not null or empty.
   *
   * @param array the array.
   * @return true if the array is not null or empty.
   * @since 10.0.0
   */
  public static boolean isNotEmpty(int @Nullable [] array) {
    return array != null && array.length > 0;
  }

  /**
   * Checks if the int array is null or empty.
   *
   * @param array the array.
   * @return true if the array is null or empty.
   * @since 10.0.0
   */
  public static boolean isEmpty(int @Nullable [] array) {
    return array == null || array.length == 0;
  }

  /**
   * Checks if the long array is not null or empty.
   *
   * @param array the array.
   * @return true if the array is not null or empty.
   * @since 10.0.0
   */
  public static boolean isNotEmpty(long @Nullable [] array) {
    return array != null && array.length > 0;
  }

  /**
   * Checks if the long array is null or empty.
   *
   * @param array the array.
   * @return true if the array is null or empty.
   * @since 10.0.0
   */
  public static boolean isEmpty(long @Nullable [] array) {
    return array == null || array.length == 0;
  }

  /**
   * Checks if the float array is not null or empty.
   *
   * @param array the array.
   * @return true if the array is not null or empty.
   * @since 10.0.0
   */
  public static boolean isNotEmpty(float @Nullable [] array) {
    return array != null && array.length > 0;
  }

  /**
   * Checks if the float array is null or empty.
   *
   * @param array the array.
   * @return true if the array is null or empty.
   * @since 10.0.0
   */
  public static boolean isEmpty(float @Nullable [] array) {
    return array == null || array.length == 0;
  }

  /**
   * Checks if the double array is not null or empty.
   *
   * @param array the array.
   * @return true if the array is not null or empty.
   * @since 10.0.0
   */
  public static boolean isNotEmpty(double @Nullable [] array) {
    return array != null && array.length > 0;
  }

  /**
   * Checks if the double array is null or empty.
   *
   * @param array the array.
   * @return true if the array is null or empty.
   * @since 10.0.0
   */
  public static boolean isEmpty(double @Nullable [] array) {
    return array == null || array.length == 0;
  }

  /**
   * Checks if the object array is not null or empty.
   *
   * @param array the array.
   * @return true if the array is not null or empty.
   * @since 10.0.0
   */
  public static boolean isNotEmpty(Object @Nullable [] array) {
    return array != null && array.length > 0;
  }

  /**
   * Checks if the object array is null or empty.
   *
   * @param array the array.
   * @return true if the array is null or empty.
   * @since 10.0.0
   */
  public static boolean isEmpty(Object @Nullable [] array) {
    return array == null || array.length == 0;
  }

  /**
   * Returns the length of the byte array or 0 if null.
   *
   * @param array the array.
   * @return the length or 0.
   * @since 10.0.0
   */
  public static int length(byte @Nullable [] array) {
    return array == null ? 0 : array.length;
  }

  /**
   * Returns the length of the short array or 0 if null.
   *
   * @param array the array.
   * @return the length or 0.
   * @since 10.0.0
   */
  public static int length(short @Nullable [] array) {
    return array == null ? 0 : array.length;
  }

  /**
   * Returns the length of the int array or 0 if null.
   *
   * @param array the array.
   * @return the length or 0.
   * @since 10.0.0
   */
  public static int length(int @Nullable [] array) {
    return array == null ? 0 : array.length;
  }

  /**
   * Returns the length of the long array or 0 if null.
   *
   * @param array the array.
   * @return the length or 0.
   * @since 10.0.0
   */
  public static int length(long @Nullable [] array) {
    return array == null ? 0 : array.length;
  }

  /**
   * Returns the length of the float array or 0 if null.
   *
   * @param array the array.
   * @return the length or 0.
   * @since 10.0.0
   */
  public static int length(float @Nullable [] array) {
    return array == null ? 0 : array.length;
  }

  /**
   * Returns the length of the double array or 0 if null.
   *
   * @param array the array.
   * @return the length or 0.
   * @since 10.0.0
   */
  public static int length(double @Nullable [] array) {
    return array == null ? 0 : array.length;
  }

  /**
   * Returns the length of the object array or 0 if null.
   *
   * @param array the array.
   * @return the length or 0.
   * @since 10.0.0
   */
  public static int length(Object @Nullable [] array) {
    return array == null ? 0 : array.length;
  }

  /**
   * Converts T array to R array using mapper.
   *
   * @param <T> the source component type.
   * @param <M> the mapped element type.
   * @param <R> the result element type.
   * @param source the source array.
   * @param mapper the mapper.
   * @param resultType the result component type.
   * @return the mapped array.
   * @since 9.5.0
   */
  public static <T, R, M extends R> R @Nullable [] map(T[] source, Function<T, M> mapper, Class<R> resultType) {
    if (isEmpty(source)) {
      return create(resultType, 0);
    }
    R[] resultArray = create(resultType, source.length);
    for (int i = 0; i < source.length; i++) {
      resultArray[i] = mapper.apply(source[i]);
    }
    return resultArray;
  }

  /**
   * Converts T array to R array using mapper with default value.
   *
   * @param <T> the source component type.
   * @param <M> the mapped element type.
   * @param <R> the result element type.
   * @param source the source array.
   * @param mapper the mapper.
   * @param def the default result if source array is null.
   * @return the mapped array.
   * @since 9.5.0
   */
  public static <T, R, M extends R> R[] map(T @Nullable [] source, Function<T, M> mapper, R[] def) {
    if (isEmpty(source)) {
      return def;
    }
    Class<?> componentType = def
        .getClass()
        .getComponentType();
    R[] resultArray = create(componentType, source.length);
    for (int i = 0; i < source.length; i++) {
      resultArray[i] = mapper.apply(source[i]);
    }
    return resultArray;
  }

  private ArrayUtils() {
    throw new RuntimeException();
  }
}
