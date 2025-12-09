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
 * @author JavaSaBr
 */
public interface Array<E> extends Iterable<E>, Serializable, Cloneable {

  static <E> Array<E> empty(Class<? super E> type) {
    return new ImmutableArray<>(ClassUtils.unsafeCast(type));
  }

  static <E> ArrayBuilder<E> builder(Class<? super E> type) {
    return new ArrayBuilder<>(type);
  }
  
  static <E> Array<E> of(E single) {
    @SuppressWarnings("unchecked")
    Class<E> type = (Class<E>) single.getClass();
    return new ImmutableArray<>(type, single);
  }

  static <E> Array<E> of(E e1, E e2) {
    Class<E> commonType = ClassUtils.commonType(e1, e2);
    return new ImmutableArray<>(commonType, e1, e2);
  }

  static <E> Array<E> of(E e1, E e2, E e3) {
    Class<E> commonType = ClassUtils.commonType(e1, e2, e3);
    return new ImmutableArray<>(commonType, e1, e2, e3);
  }

  static <E> Array<E> of(E e1, E e2, E e3, E e4) {
    Class<E> commonType = ClassUtils.commonType(e1, e2, e3, e4);
    return new ImmutableArray<>(commonType, e1, e2, e3, e4);
  }

  static <E> Array<E> of(E e1, E e2, E e3, E e4, E e5) {
    Class<E> commonType = ClassUtils.commonType(e1, e2, e3, e4, e5);
    return new ImmutableArray<>(commonType, e1, e2, e3, e4, e5);
  }

  static <E> Array<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
    Class<E> commonType = ClassUtils.commonType(e1, e2, e3, e4, e5, e6);
    return new ImmutableArray<>(commonType, e1, e2, e3, e4, e5, e6);
  }

  @SafeVarargs
  static <E> Array<E> of(E... elements) {
    //noinspection unchecked
    Class<E> type = (Class<E>) elements
        .getClass()
        .getComponentType();
    return new ImmutableArray<>(type, elements);
  }

  @SafeVarargs
  static <E> Array<E> typed(Class<? super E> type, E... elements) {
    return new ImmutableArray<>(type, elements);
  }

  @SafeVarargs
  static <E> Array<E> optionals(Class<? super E> type, Optional<E>... elements) {
    return Stream
        .of(elements)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(ArrayCollectors.toArray(type));
  }

  /**
   * Creates array with the same element repeated 'count' times
   */
  static <E> Array<E> repeated(E element, int count) {
    @SuppressWarnings("unchecked")
    Class<E> type = (Class<E>) element.getClass();
    E[] array = ArrayUtils.create(type, count);
    Arrays.fill(array, element);
    return ImmutableArray.trustWrap(array);
  }

  static <E> Array<E> copyOf(Array<E> array) {
    if (array instanceof ImmutableArray<E>) {
      return array;
    }
    return new ImmutableArray<>(array.type(), array.toArray());
  }

  static <E> Array<E> copyOf(Class<? super E> type, Collection<E> collection) {
    if (collection instanceof MutableArray<E> mutableArray) {
      return copyOf(mutableArray);
    }
    E[] array = collection.toArray(ArrayUtils.create(type, collection.size()));
    return ImmutableArray.trustWrap(array);
  }

  Class<E> type();

  /**
   * Returns the number of elements in this array.  If this array
   * contains more than {@code Integer.MAX_VALUE} elements, returns
   * {@code Integer.MAX_VALUE}.
   *
   * @return the number of elements in this array
   */
  int size();

  /**
   * Returns {@code true} if this array contains the specified element.
   * More formally, returns {@code true} if and only if this array
   * contains at least one element {@code e} such that
   * {@code Objects.equals(object, e)}.
   *
   * @param object element whose presence in this array is to be tested
   * @return {@code true} if this array contains the specified
   *         element
   * @throws ClassCastException if the type of the specified element
   *         is incompatible with this array
   *         ({@linkplain Collection##optional-restrictions optional})
   * @throws NullPointerException if the specified element is null and this
   *         array does not permit null elements
   *         ({@linkplain Collection##optional-restrictions optional})
   */
  boolean contains(@Nullable Object object);

  /**
   * Returns {@code true} if this array contains all of the elements
   * in the specified array.
   *
   * @param  array array to be checked for containment in this collection
   * @return {@code true} if this array contains all of the elements
   *         in the specified array
   * @throws ClassCastException if the types of one or more elements
   *         in the specified array are incompatible with this
   *         array
   *         ({@linkplain Collection##optional-restrictions optional})
   * @throws NullPointerException if the specified array contains one
   *         or more null elements and this array does not permit null
   *         elements
   *         ({@linkplain Collection##optional-restrictions optional})
   *         or if the specified collection is null.
   * @see    #contains(Object)
   */
  boolean containsAll(Array<?> array);

  /**
   * Returns {@code true} if this array contains all of the elements
   * in the specified collection.
   *
   * @param  collection collection to be checked for containment in this array
   * @return {@code true} if this array contains all of the elements
   *         in the specified collection
   * @throws ClassCastException if the types of one or more elements
   *         in the specified collection are incompatible with this
   *         collection
   *         ({@linkplain Collection##optional-restrictions optional})
   * @throws NullPointerException if the specified collection contains one
   *         or more null elements and this array does not permit null
   *         elements
   *         ({@linkplain Collection##optional-restrictions optional})
   *         or if the specified collection is null.
   * @see    #contains(Object)
   */
  boolean containsAll(Collection<?> collection);

  /**
   * Returns {@code true} if this array contains all of the elements
   * in the specified array.
   *
   * @param  array array to be checked for containment in this collection
   * @return {@code true} if this array contains all of the elements
   *         in the specified array
   * @throws ClassCastException if the types of one or more elements
   *         in the specified array are incompatible with this
   *         array
   *         ({@linkplain Collection##optional-restrictions optional})
   * @throws NullPointerException if the specified array contains one
   *         or more null elements and this array does not permit null
   *         elements
   *         ({@linkplain Collection##optional-restrictions optional})
   *         or if the specified collection is null.
   * @see    #contains(Object)
   */
  boolean containsAll(Object[] array);

  /**
   * Gets the first element of this array.

   * @return the retrieved element or null
   */
  @Nullable
  E first();

  E get(int index);

  /**
   * Gets the last element of this array.

   * @return the retrieved element or null
   */
  @Nullable
  E last();

  @Override
  default Iterator<E> iterator() {
    return new DefaultArrayIterator<>(this);
  }

  /**
   * @return the index of the object or -1.
   */
  int indexOf(@Nullable Object object);

  /**
   * @return the index of the object or -1.
   */
  <T> int indexOf(Function<E, T> getter, @Nullable Object object);

  int lastIndexOf(@Nullable Object object);

  <T > T[] toArray(T[] newArray);

  /**
   * Copy this array to the new array.
   *
   * @param <T> the type parameter
   * @param componentType the type of the new array.
   * @return the copied array.
   */
  <T> T[] toArray(Class<T> componentType);

  boolean isEmpty();

  E[] toArray();

  String toString(Function<E, String> toString);

  /**
   * Returns a sequential {@code Stream} with this array as its source.
   *
   * @return a sequential {@code Stream} over the elements in this array
   */
  Stream<E> stream();

  ArrayIterationFunctions<E> iterations();

  UnsafeArray<E> asUnsafe();

  List<E> toList();
}
