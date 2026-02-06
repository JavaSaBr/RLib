package javasabr.rlib.collections.array;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import javasabr.rlib.collections.array.impl.DefaultArrayIterator;
import javasabr.rlib.collections.array.impl.ImmutableArray;
import javasabr.rlib.common.util.ArrayUtils;
import javasabr.rlib.common.util.ClassUtils;
import org.jspecify.annotations.Nullable;

/**
 * An immutable array interface that provides type-safe, indexed access to elements.
 *
 * @param <E> the type of elements in this array
 * @author JavaSaBr
 * @since 10.0.0
 */
public interface Array<E> extends Iterable<E>, Serializable, Cloneable {

  /**
   * Creates an empty immutable array of the specified type.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @return an empty immutable array
   * @since 10.0.0
   */
  static <E> Array<E> empty(Class<? super E> type) {
    return new ImmutableArray<>(ClassUtils.unsafeCast(type));
  }

  /**
   * Creates a new builder for constructing an immutable array.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @return a new array builder
   * @since 10.0.0
   */
  static <E> ArrayBuilder<E> builder(Class<? super E> type) {
    return new ArrayBuilder<>(type);
  }

  /**
   * Creates an immutable array containing a single element.
   *
   * @param <E> the type of elements
   * @param single the single element
   * @return an immutable array containing the element
   * @since 10.0.0
   */
  static <E> Array<E> of(E single) {
    @SuppressWarnings("unchecked")
    Class<E> type = (Class<E>) single.getClass();
    return new ImmutableArray<>(type, single);
  }

  /**
   * Creates an immutable array containing two elements.
   *
   * @param <E> the type of elements
   * @param e1 the first element
   * @param e2 the second element
   * @return an immutable array containing the elements
   * @since 10.0.0
   */
  static <E> Array<E> of(E e1, E e2) {
    Class<E> commonType = ClassUtils.commonType(e1, e2);
    return new ImmutableArray<>(commonType, e1, e2);
  }

  /**
   * Creates an immutable array containing three elements.
   *
   * @param <E> the type of elements
   * @param e1 the first element
   * @param e2 the second element
   * @param e3 the third element
   * @return an immutable array containing the elements
   * @since 10.0.0
   */
  static <E> Array<E> of(E e1, E e2, E e3) {
    Class<E> commonType = ClassUtils.commonType(e1, e2, e3);
    return new ImmutableArray<>(commonType, e1, e2, e3);
  }

  /**
   * Creates an immutable array containing four elements.
   *
   * @param <E> the type of elements
   * @param e1 the first element
   * @param e2 the second element
   * @param e3 the third element
   * @param e4 the fourth element
   * @return an immutable array containing the elements
   * @since 10.0.0
   */
  static <E> Array<E> of(E e1, E e2, E e3, E e4) {
    Class<E> commonType = ClassUtils.commonType(e1, e2, e3, e4);
    return new ImmutableArray<>(commonType, e1, e2, e3, e4);
  }

  /**
   * Creates an immutable array containing five elements.
   *
   * @param <E> the type of elements
   * @param e1 the first element
   * @param e2 the second element
   * @param e3 the third element
   * @param e4 the fourth element
   * @param e5 the fifth element
   * @return an immutable array containing the elements
   * @since 10.0.0
   */
  static <E> Array<E> of(E e1, E e2, E e3, E e4, E e5) {
    Class<E> commonType = ClassUtils.commonType(e1, e2, e3, e4, e5);
    return new ImmutableArray<>(commonType, e1, e2, e3, e4, e5);
  }

  /**
   * Creates an immutable array containing six elements.
   *
   * @param <E> the type of elements
   * @param e1 the first element
   * @param e2 the second element
   * @param e3 the third element
   * @param e4 the fourth element
   * @param e5 the fifth element
   * @param e6 the sixth element
   * @return an immutable array containing the elements
   * @since 10.0.0
   */
  static <E> Array<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
    Class<E> commonType = ClassUtils.commonType(e1, e2, e3, e4, e5, e6);
    return new ImmutableArray<>(commonType, e1, e2, e3, e4, e5, e6);
  }

  /**
   * Creates an immutable array containing the specified elements.
   *
   * @param <E> the type of elements
   * @param elements the elements to include
   * @return an immutable array containing the elements
   * @since 10.0.0
   */
  @SafeVarargs
  static <E> Array<E> of(E... elements) {
    //noinspection unchecked
    Class<E> type = (Class<E>) elements
        .getClass()
        .getComponentType();
    return new ImmutableArray<>(type, elements);
  }

  /**
   * Creates an immutable array with explicit type containing the specified elements.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @param elements the elements to include
   * @return an immutable array containing the elements
   * @since 10.0.0
   */
  @SafeVarargs
  static <E> Array<E> typed(Class<? super E> type, E... elements) {
    return new ImmutableArray<>(type, elements);
  }

  /**
   * Creates an immutable array from present optional values.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @param elements the optional elements
   * @return an immutable array containing present values
   * @since 10.0.0
   */
  @SafeVarargs
  static <E> Array<E> optionals(Class<? super E> type, Optional<E>... elements) {
    return Stream
        .of(elements)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(ArrayCollectors.toArray(type));
  }

  /**
   * Creates an immutable array with the same element repeated specified times.
   *
   * @param <E> the type of elements
   * @param element the element to repeat
   * @param count the number of times to repeat
   * @return an immutable array with repeated elements
   * @since 10.0.0
   */
  static <E> Array<E> repeated(E element, int count) {
    @SuppressWarnings("unchecked")
    Class<E> type = (Class<E>) element.getClass();
    E[] array = ArrayUtils.create(type, count);
    Arrays.fill(array, element);
    return ImmutableArray.trustWrap(array);
  }

  /**
   * Creates an immutable copy of the specified array.
   *
   * @param <E> the type of elements
   * @param array the array to copy
   * @return an immutable copy of the array
   * @since 10.0.0
   */
  static <E> Array<E> copyOf(Array<E> array) {
    if (array instanceof ImmutableArray<E>) {
      return array;
    }
    return new ImmutableArray<>(array.type(), array.toArray());
  }

  /**
   * Creates an immutable array from the specified collection.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @param collection the collection to copy
   * @return an immutable array containing the collection elements
   * @since 10.0.0
   */
  static <E> Array<E> copyOf(Class<? super E> type, Collection<E> collection) {
    if (collection instanceof MutableArray<E> mutableArray) {
      return copyOf(mutableArray);
    }
    E[] array = collection.toArray(ArrayUtils.create(type, collection.size()));
    return ImmutableArray.trustWrap(array);
  }

  /**
   * Returns the component type of this array.
   *
   * @return the component type
   * @since 10.0.0
   */
  Class<E> type();

  /**
   * Returns the number of elements in this array. If this array
   * contains more than {@code Integer.MAX_VALUE} elements, returns
   * {@code Integer.MAX_VALUE}.
   *
   * @return the number of elements in this array
   * @since 10.0.0
   */
  int size();

  /**
   * Returns {@code true} if this array contains the specified element.
   * More formally, returns {@code true} if and only if this array
   * contains at least one element {@code e} such that
   * {@code Objects.equals(object, e)}.
   *
   * @param object element whose presence in this array is to be tested
   * @return {@code true} if this array contains the specified element
   * @since 10.0.0
   */
  boolean contains(@Nullable Object object);

  /**
   * Returns {@code true} if this array contains all of the elements
   * in the specified array.
   *
   * @param  array array to be checked for containment in this array
   * @return {@code true} if this array contains all of the elements
   *         in the specified array
   * @see    #contains(Object)
   * @since 10.0.0
   */
  boolean containsAll(Array<?> array);

  /**
   * Returns {@code true} if this array contains all of the elements
   * in the specified collection.
   *
   * @param  collection collection to be checked for containment in this array
   * @return {@code true} if this array contains all of the elements
   *         in the specified collection
   * @see    #contains(Object)
   * @since 10.0.0
   */
  boolean containsAll(Collection<?> collection);

  /**
   * Returns {@code true} if this array contains all of the elements
   * in the specified array.
   *
   * @param  array array to be checked for containment in this array
   * @return {@code true} if this array contains all of the elements
   *         in the specified array
   * @see    #contains(Object)
   * @since 10.0.0
   */
  boolean containsAll(Object[] array);

  /**
   * Returns the first element of this array, or null if empty.
   *
   * @return the first element or null
   * @since 10.0.0
   */
  @Nullable
  E first();

  /**
   * Returns the element at the specified index.
   *
   * @param index the index of the element to return
   * @return the element at the specified index
   * @throws IndexOutOfBoundsException if the index is out of range
   * @since 10.0.0
   */
  E get(int index);

  /**
   * Returns the last element of this array, or null if empty.
   *
   * @return the last element or null
   * @since 10.0.0
   */
  @Nullable
  E last();

  @Override
  default Iterator<E> iterator() {
    return new DefaultArrayIterator<>(this);
  }

  /**
   * Returns the index of the first occurrence of the specified object.
   *
   * @param object the object to search for
   * @return the index of the object or -1 if not found
   * @since 10.0.0
   */
  int indexOf(@Nullable Object object);

  /**
   * Returns the index of the first element whose property matches the specified object.
   *
   * @param <T> the type of the property
   * @param getter the function to extract the property
   * @param object the object to match
   * @return the index of the object or -1 if not found
   * @since 10.0.0
   */
  <T> int indexOf(Function<E, T> getter, @Nullable Object object);

  /**
   * Returns the index of the last occurrence of the specified object.
   *
   * @param object the object to search for
   * @return the index of the object or -1 if not found
   * @since 10.0.0
   */
  int lastIndexOf(@Nullable Object object);

  /**
   * Copies elements into the provided array.
   *
   * @param <T> the type of the array elements
   * @param newArray the array to copy into
   * @return the array with copied elements
   * @since 10.0.0
   */
  <T > T[] toArray(T[] newArray);

  /**
   * Copies this array to a new array of the specified component type.
   *
   * @param <T> the type parameter
   * @param componentType the type of the new array
   * @return the copied array
   * @since 10.0.0
   */
  <T> T[] toArray(Class<T> componentType);

  /**
   * Returns {@code true} if this array contains no elements.
   *
   * @return {@code true} if this array is empty
   * @since 10.0.0
   */
  boolean isEmpty();

  /**
   * Returns a new array containing all elements.
   *
   * @return an array containing all elements
   * @since 10.0.0
   */
  E[] toArray();

  /**
   * Returns a string representation using the specified formatter.
   *
   * @param toString the function to convert elements to strings
   * @return a string representation of this array
   * @since 10.0.0
   */
  String toString(Function<E, String> toString);

  /**
   * Returns a sequential {@code Stream} with this array as its source.
   *
   * @return a sequential {@code Stream} over the elements in this array
   * @since 10.0.0
   */
  Stream<E> stream();

  /**
   * Returns iteration functions for this array.
   *
   * @return the iteration functions
   * @since 10.0.0
   */
  ArrayIterationFunctions<E> iterations();

  /**
   * Returns an unsafe view of this array providing direct access to internals.
   *
   * @return an unsafe view of this array
   * @since 10.0.0
   */
  UnsafeArray<E> asUnsafe();

  /**
   * Returns an unmodifiable list view of this array.
   *
   * @return an unmodifiable list view
   * @since 10.0.0
   */
  List<E> toList();
}
