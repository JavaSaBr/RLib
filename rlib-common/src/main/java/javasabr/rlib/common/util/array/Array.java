package javasabr.rlib.common.util.array;

import static javasabr.rlib.common.util.ClassUtils.unsafeCast;
import static javasabr.rlib.common.util.ClassUtils.unsafeNNCast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javasabr.rlib.common.function.BiIntPredicate;
import javasabr.rlib.common.function.NotNullBiConsumer;
import javasabr.rlib.common.function.NotNullBiPredicate;
import javasabr.rlib.common.function.NotNullConsumer;
import javasabr.rlib.common.function.NotNullFloatBiObjectConsumer;
import javasabr.rlib.common.function.NotNullFunction;
import javasabr.rlib.common.function.NotNullFunctionInt;
import javasabr.rlib.common.function.NotNullIntBiObjectConsumer;
import javasabr.rlib.common.function.NotNullIntObjectPredicate;
import javasabr.rlib.common.function.NotNullLongBiObjectConsumer;
import javasabr.rlib.common.function.NotNullLongObjectPredicate;
import javasabr.rlib.common.function.NotNullPredicate;
import javasabr.rlib.common.function.NotNullSupplier;
import javasabr.rlib.common.function.NotNullTripleConsumer;
import javasabr.rlib.common.function.NotNullTriplePredicate;
import javasabr.rlib.common.util.ArrayUtils;
import javasabr.rlib.common.util.ClassUtils;
import javasabr.rlib.common.util.array.impl.DefaultArrayIterator;
import javasabr.rlib.common.util.pools.Reusable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Interface to implement dynamic arrays.
 * <p> To create to use {@link ArrayFactory}.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
@NullMarked
public interface Array<E> extends Collection<E>, Serializable, Reusable, Cloneable, RandomAccess {

  /**
   * Create an empty read only array.
   *
   * @param <T> the result element's type.
   * @return the empty array.
   */
  static <T> ReadOnlyArray<T> empty() {
    return unsafeNNCast(ArrayFactory.EMPTY_ARRAY);
  }

  /**
   * Create a new array for the element's type.
   *
   * @param type the element's type.
   * @param <T> the element's type.
   * @return the new array.
   */
  static <T> Array<T> ofType(Class<? super T> type) {
    return ArrayFactory.newArray(type);
  }

  /**
   * Create a new array for the element's type.
   *
   * @param type the element's type.
   * @param capacity the start capacity of this array.
   * @param <T> the element's type.
   * @return the new array.
   */
  static <T> Array<T> ofType(Class<? super T> type, int capacity) {
    return ArrayFactory.newArray(type, capacity);
  }

  /**
   * Copy an array to a read only array.
   *
   * @param another the another array.
   * @param <T> the element's type.
   * @return the new read only array.
   */
  static <T> ReadOnlyArray<T> of(Array<T> another) {
    return ArrayFactory.newReadOnlyArray(ArrayUtils.copyOfRange(another.array(), 0, another.size()));
  }

  /**
   * Creates a single element read only array.
   *
   * @param element the element.
   * @param <T> the element's type.
   * @return the read only array.
   */
  static <T> ReadOnlyArray<T> of(T element) {

    T[] newArray = ArrayUtils.create(element.getClass(), 1);
    newArray[0] = element;

    return ArrayFactory.newReadOnlyArray(newArray);
  }

  @SafeVarargs
  static <T> ReadOnlyArray<T> of(T... elements) {
    return ArrayFactory.newReadOnlyArray(ArrayUtils.copyOf(elements));
  }

  @SafeVarargs
  static <T> ReadOnlyArray<T> optionals(Class<? super T> type, Optional<T>... elements) {
    return ArrayFactory.newReadOnlyArray(Arrays
        .stream(elements)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toArray(value -> ArrayUtils.create(type, value)));
  }

  static <T, A extends Array<T>> A append(A first, A second) {
    first.addAll(second);
    return first;
  }

  static <T> ReadOnlyArray<T> combine(Array<T> first, Array<T> second) {

    var componentType = ClassUtils.<Class<T>>unsafeNNCast(first
        .array()
        .getClass()
        .getComponentType());

    var newArray = ArrayUtils.combine(first.toArray(componentType), second.toArray(componentType));

    return ArrayFactory.newReadOnlyArray(newArray);
  }

  /**
   * Create a supplier which creates new arrays.
   *
   * @param type the element's type.
   * @param <T> the element's type.
   * @return the supplier.
   */
  static <T> NotNullSupplier<Array<T>> supplier(Class<? super T> type) {
    return () -> ArrayFactory.newConcurrentStampedLockArray(type);
  }

  /**
   * Create a function which creates new arrays.
   *
   * @param type the element's type.
   * @param <T> the element's type.
   * @return the supplier.
   */
  static <T> NotNullFunction<Class<? super T>, Array<T>> function(Class<? super T> type) {
    return ArrayFactory::newConcurrentStampedLockArray;
  }

  /**
   * Copy all elements from this array to a target array.
   *
   * @param target the target array.
   */
  default void copyTo(Array<? super E> target) {
    target.addAll(this);
  }

  /**
   * Adds all elements from the array to this array.
   *
   * @param array the array with new elements.
   * @return true if this array was changed.
   */
  boolean addAll(Array<? extends E> array);

  /**
   * Adds all elements from the array to this array.
   *
   * @param array the array with new elements.
   * @return true if this array was changed.
   */
  boolean addAll(E[] array);

  /**
   * Applies this function to each element of this array with replacing to result element from thia function.
   *
   * @param function the function.
   */
  default void apply(Function<? super E, ? extends E> function) {

    E[] array = array();

    for (int i = 0, length = size(); i < length; i++) {
      array[i] = function.apply(array[i]);
    }
  }

  /**
   * Get the wrapped array.
   *
   * @return the wrapped array.
   */
  E [] array();

  @Override
  default Stream<E> stream() {
    return Arrays.stream(array(), 0, size());
  }

  @Override
  default Stream<E> parallelStream() {
    return stream().parallel();
  }

  @Override
  default boolean contains(Object object) {

    for (E element : array()) {
      if (element == null) {
        break;
      } else if (element.equals(object)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns true if this array contains all of the elements in the specified array.
   *
   * @param array the array to be checked for containment in this array
   * @return true if this array contains all of the elements in the specified array.
   */
  default boolean containsAll(Array<?> array) {

    if (array.isEmpty()) {
      return false;
    }

    for (var element : array.array()) {
      if (element == null) {
        break;
      } else if (!contains(element)) {
        return false;
      }
    }

    return true;
  }

  @Override
  default boolean containsAll(Collection<?> array) {

    if (array.isEmpty()) {
      return false;
    }

    for (var element : array) {
      if (element == null) {
        break;
      } else if (!contains(element)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns true if this array contains all of the elements in the specified array.
   *
   * @param array the array to be checked for containment in this array
   * @return true if this array contains all of the elements in the specified array.
   */
  default boolean containsAll(Object[] array) {

    if (array.length < 1) {
      return false;
    }

    for (Object element : array) {
      if (!contains(element)) {
        return false;
      }
    }

    return true;
  }

  @Override
  default void free() {
    clear();
  }

  /**
   * Removes the element at index possible with reordering.
   *
   * @param index the index of removing the element.
   * @return the removed element.
   */
  E fastRemove(int index);

  /**
   * Removes an element without saving original ordering of other elements.
   *
   * @param object the element to remove.
   * @return <code>true</code> if the element was removed.
   */
  default boolean fastRemove(Object object) {

    int index = indexOf(object);

    if (index >= 0) {
      fastRemove(index);
    }

    return index >= 0;
  }

  /**
   * Removes the each element from the array.
   *
   * @param array the array with elements to remove.
   * @return count of removed elements.
   */
  default int fastRemoveAll(E[] array) {

    int count = 0;

    for (var object : array) {
      if (fastRemove(object)) {
        count++;
      }
    }

    return count;
  }

  /**
   * Try to get the first element of this array.
   *
   * @return the first element or null.
   */
  default @Nullable E first() {
    return isEmpty() ? null : get(0);
  }

  /**
   * Get an element by an index.
   *
   * @param index the index of the element.
   * @return the element.
   */
  E get(int index);

  /**
   * Find an index of the object in this array.
   *
   * @param object the object to find.
   * @return the index of the object or -1.
   */
  default int indexOf(Object object) {

    int index = 0;

    for (E element : array()) {

      if (element == null) {
        break;
      } else if (Objects.equals(object, element)) {
        return index;
      }

      index++;
    }

    return -1;
  }

  @Override
  default ArrayIterator<E> iterator() {
    return new DefaultArrayIterator<>(this);
  }

  /**
   * Try to get the last element.
   *
   * @return the last element or null.
   */
  default @Nullable E last() {

    int size = size();

    if (size < 1) {
      return null;
    }

    return get(size - 1);
  }

  /**
   * Find the last index of the object in this array.
   *
   * @param object the object.
   * @return the last index or -1.
   */
  default int lastIndexOf(Object object) {

    E[] array = array();

    int last = -1;

    for (int i = 0, length = size(); i < length; i++) {

      E element = array[i];

      if (element.equals(object)) {
        last = i;
      }
    }

    return last;
  }

  /**
   * Get and remove the first element from this array.
   *
   * @return the first element or null.
   */
  default @Nullable E poll() {
    return isEmpty() ? null : remove(0);
  }

  /**
   * Get and remove the last element of this array.
   *
   * @return the last element or null.
   */
  default @Nullable E pop() {
    return isEmpty() ? null : fastRemove(size() - 1);
  }

  /**
   * Removes all of this target's elements that are also contained in the specified array (optional operation).
   *
   * @param target array containing elements to be removed from this array.
   * @return true if this array changed as a result of the call.
   */
  default boolean removeAll(Array<?> target) {

    if (target.isEmpty()) {
      return false;
    }

    int count = 0;

    for (var element : target.array()) {
      if (element == null) {
        break;
      } else if (remove(element)) {
        count++;
      }
    }

    return count > 0;
  }

  /**
   * Removes all of this target's elements that are also contained in the specified array (optional operation) with
   * reordering.
   *
   * @param target array containing elements to be removed from this array.
   * @return true if this array changed as a result of the call.
   */
  default boolean fastRemoveAll(Array<?> target) {

    if (target.isEmpty()) {
      return false;
    }

    var count = 0;
    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (!target.contains(element)) {
        continue;
      }

      fastRemove(i);
      i--;
      length--;
      count++;
    }

    return count > 0;
  }

  @Override
  default boolean removeAll(Collection<?> target) {

    if (target.isEmpty()) {
      return false;
    }

    int count = 0;

    for (var element : target) {
      if (remove(element)) {
        count++;
      }
    }

    return count == target.size();
  }

  /**
   * Retains only the elements in this array that are contained in the specified array (optional operation).  In other
   * words, removes from this array all of its elements that are not contained in the specified array.
   *
   * @param target array containing elements to be retained in this array.
   * @return true if this array changed as a result of the call.
   */
  default boolean retainAll(Array<?> target) {

    E[] array = array();

    for (int i = 0, length = size(); i < length; i++) {
      if (!target.contains(array[i])) {
        fastRemove(i--);
        length--;
      }
    }

    return true;
  }

  @Override
  default boolean retainAll(Collection<?> target) {

    E[] array = array();

    for (int i = 0, length = size(); i < length; i++) {
      if (!target.contains(array[i])) {
        fastRemove(i--);
        length--;
      }
    }

    return true;
  }

  /**
   * Replace an element by an index.
   *
   * @param index the element's index.
   * @param element the new element.
   */
  void replace(int index, E element);

  /**
   * Removes the element at index.
   *
   * @param index the index of removing the element.
   * @return the removed element.
   */
  E remove(int index);

  @Override
  default boolean remove(Object object) {

    var index = indexOf(object);

    if (index >= 0) {
      remove(index);
    }

    return index >= 0;
  }

  /**
   * Sort this array using the comparator.
   *
   * @param comparator the comparator.
   * @return the array
   */
  default Array<E> sort(ArrayComparator<E> comparator) {
    ArrayUtils.sort(array(), 0, size(), comparator);
    return this;
  }

  @Override
  default <T> T[] toArray(T[] newArray) {

    E[] array = array();

    if (newArray.length >= size()) {

      for (int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {
        if (array[i] == null) {
          continue;
        }
        newArray[j++] = unsafeCast(array[i]);
      }

      return newArray;
    }

    Class<T[]> arrayClass = unsafeCast(newArray.getClass());
    Class<T> componentType = unsafeCast(arrayClass.getComponentType());

    return toArray(componentType);
  }

  /**
   * Copy this array to the new array.
   *
   * @param <T> the type parameter
   * @param componentType the type of the new array.
   * @return the copied array.
   */
  default <T> T[] toArray(Class<T> componentType) {

    T[] newArray = ArrayUtils.create(componentType, size());
    E[] array = array();

    System.arraycopy(array, 0, newArray, 0, size());

    return newArray;
  }

  /**
   * Get the unsafe interface of this array.
   *
   * @return the unsafe interface of this array.
   */
  default UnsafeArray<E> asUnsafe() {
    if (this instanceof UnsafeArray) {
      return (UnsafeArray<E>) this;
    } else {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  default boolean isEmpty() {
    return size() < 1;
  }

  @Override
  default E[] toArray() {
    var array = array();
    return Arrays.copyOf(array, size(), (Class<E[]>) array.getClass());
  }

  String toString(Function<E, String> toString);

  @Override
  default boolean removeIf(Predicate<? super E> filter) {

    var array = array();
    var removed = 0;

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(element)) {
        remove(i);
        i--;
        length--;
        removed++;
      }
    }

    return removed > 0;
  }

  /**
   * Removes all of the elements of this collection that satisfy the given predicate.
   *
   * @param argument the additional argument.
   * @param filter the predicate which returns {@code true} for elements to be removed.
   * @param <A> the argument's type.
   * @return {@code true} if any elements were removed.
   */
  default <A> boolean removeIf(A argument, NotNullBiPredicate<A, ? super E> filter) {

    var array = array();
    var removed = 0;

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(argument, element)) {
        remove(i);
        i--;
        length--;
        removed++;
      }
    }

    return removed > 0;
  }

  /**
   * Removes all of the elements of this collection that satisfy the given predicate.
   *
   * @param argument the additional argument.
   * @param converter the converter of the argument.
   * @param filter the predicate which returns {@code true} for elements to be removed.
   * @param <A> the argument's type.
   * @param <B> the argument converted type.
   * @return {@code true} if any elements were removed.
   */
  default <A, B> boolean removeIf(
      A argument,
      NotNullFunction<A, B> converter,
      NotNullBiPredicate<B, ? super E> filter) {

    var array = array();
    var removed = 0;

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(converter.apply(argument), element)) {
        remove(i);
        i--;
        length--;
        removed++;
      }
    }

    return removed > 0;
  }

  /**
   * Removes all of the elements of this collection that satisfy the given predicate.
   *
   * @param argument the additional argument.
   * @param converter the converter of the elements.
   * @param filter the predicate which returns {@code true} for elements to be removed.
   * @param <A> the argument's type.
   * @param <B> the element converted type.
   * @return {@code true} if any elements were removed.
   * @since 9.6.0
   */
  default <A, B> boolean removeIfConverted(
      A argument,
      NotNullFunction<? super E, B> converter,
      NotNullBiPredicate<A, B> filter) {

    var array = array();
    var removed = 0;

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(argument, converter.apply(element))) {
        remove(i);
        i--;
        length--;
        removed++;
      }
    }

    return removed > 0;
  }

  /**
   * Return true if there is at least an element for the condition.
   *
   * @param argument the argument.
   * @param filter the condition.
   * @param <T> the argument's type.
   * @return true if there is at least an element for the condition.
   */
  default <T> boolean anyMatch(T argument, NotNullBiPredicate<T, ? super E> filter) {
    return findAny(argument, filter) != null;
  }

  /**
   * Return true if there is at least a converted element for the condition.
   *
   * @param argument the argument.
   * @param converter the converter element to another type.
   * @param filter the condition.
   * @param <T> the argument's type.
   * @param <C> the converted element's type.
   * @return true if there is at least an element for the condition.
   * @since 9.7.0
   */
  default <T, C> boolean anyMatchConverted(
      T argument,
      NotNullFunction<? super E, C> converter,
      NotNullBiPredicate<T, C> filter) {
    return findAnyConverted(argument, converter, filter) != null;
  }

  /**
   * Return true if there is at least an element for the condition.
   *
   * @param argument the argument.
   * @param filter the condition.
   * @return true if there is at least an element for the condition.
   */
  default boolean anyMatch(int argument, NotNullIntObjectPredicate<? super E> filter) {
    return findAny(argument, filter) != null;
  }

  /**
   * Return true if there is at least an element for the condition.
   *
   * @param <T> the argument's type.
   * @param argument the argument.
   * @param filter the condition.
   * @return true if there is at least an element for the condition.
   */
  default <T> boolean anyMatchR(T argument, NotNullBiPredicate<? super E, T> filter) {
    return findAnyR(argument, filter) != null;
  }

  /**
   * Find an element using the condition.
   *
   * @param filter the condition.
   * @return the found element or null.
   */
  default @Nullable E findAny(NotNullPredicate<E> filter) {

    if (isEmpty()) {
      return null;
    }

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(element)) {
        return element;
      }
    }

    return null;
  }

  /**
   * Search an element using the condition.
   *
   * @param argument the argument.
   * @param filter the condition.
   * @param <T> the argument's type.
   * @return the found element or null.
   */
  default <T> @Nullable E findAny(T argument, NotNullBiPredicate<T, ? super E> filter) {

    if (isEmpty()) {
      return null;
    }

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(argument, element)) {
        return element;
      }
    }

    return null;
  }

  /**
   * Search an element using the condition by converted value.
   *
   * @param argument the argument.
   * @param converter the converted an element to another type.
   * @param filter the condition.
   * @param <T> the argument's type.
   * @param <C> the converted element's type.
   * @return the found element or null.
   * @since 9.7.0
   */
  default <T, C> @Nullable E findAnyConverted(
      T argument,
      NotNullFunction<? super E, C> converter,
      NotNullBiPredicate<T, C> filter) {

    if (isEmpty()) {
      return null;
    }

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(argument, converter.apply(element))) {
        return element;
      }
    }

    return null;
  }

  /**
   * Find an element for the condition.
   *
   * @param <T> the argument's type.
   * @param argument the argument.
   * @param filter the condition.
   * @return the found element or null.
   */
  default <T> @Nullable E findAnyR(T argument, NotNullBiPredicate<? super E, T> filter) {

    if (isEmpty()) {
      return null;
    }

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(element, argument)) {
        return element;
      }
    }

    return null;
  }

  /**
   * Search an element using the condition.
   *
   * @param argument the argument.
   * @param filter the condition.
   * @return the found element or null.
   */
  default @Nullable E findAny(int argument, NotNullIntObjectPredicate<? super E> filter) {

    if (isEmpty()) {
      return null;
    }

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(argument, element)) {
        return element;
      }
    }

    return null;
  }

  /**
   * Search an element using the condition.
   *
   * @param argument the argument.
   * @param filter the condition.
   * @return the found element or null.
   */
  default @Nullable E findAnyL(long argument, NotNullLongObjectPredicate<E> filter) {

    if (isEmpty()) {
      return null;
    }

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(argument, element)) {
        return element;
      }
    }

    return null;
  }

  /**
   * Search an element using the condition.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param filter the condition.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @return the found element or null.
   */
  default <F, S> @Nullable E findAny(
      F first,
      S second,
      NotNullTriplePredicate<F, S, E> filter) {

    if (isEmpty()) {
      return null;
    }

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(first, second, element)) {
        return element;
      }
    }

    return null;
  }

  /**
   * Search a converted element to int using the condition.
   *
   * @param argument the argument.
   * @param converter the converter element to int.
   * @param filter the condition.
   * @return the found element or null.
   * @since 9.6.0
   */
  default @Nullable E findAnyConvertedToInt(
      int argument,
      NotNullFunctionInt<? super E> converter,
      BiIntPredicate filter) {

    if (isEmpty()) {
      return null;
    }

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(argument, converter.apply(element))) {
        return element;
      }
    }

    return null;
  }

  /**
   * Search a converted element to int using the condition.
   *
   * @param argument the argument.
   * @param firstConverter the converter element to T.
   * @param secondConverter the converter element to int.
   * @param filter the condition.
   * @param <T> the first converted type.
   * @return the found element or null.
   * @since 9.7.0
   */
  default <T> @Nullable E findAnyConvertedToInt(
      int argument,
      NotNullFunction<? super E, T> firstConverter,
      NotNullFunctionInt<T> secondConverter,
      BiIntPredicate filter) {

    if (isEmpty()) {
      return null;
    }

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(argument, secondConverter.apply(firstConverter.apply(element)))) {
        return element;
      }
    }

    return null;
  }

  /**
   * Calculate a count of matched elements.
   *
   * @param filter the condition.
   * @return the count of matched elements.
   * @since 9.5.0
   */
  default int count(NotNullPredicate<E> filter) {

    var array = array();
    var count = 0;

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(element)) {
        count++;
      }
    }

    return count;
  }

  /**
   * Calculate a count of matched elements.
   *
   * @param arg the argument.
   * @param filter the condition.
   * @param <F> the argument's type.
   * @return the count of matched elements.
   */
  default <F> int count(F arg, NotNullBiPredicate<F, E> filter) {

    var array = array();
    var count = 0;

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(arg, element)) {
        count++;
      }
    }

    return count;
  }

  /**
   * Calculate a count of matched elements with reversed ordering of arguments.
   *
   * @param <F> the argument's type.
   * @param arg the argument.
   * @param filter the condition.
   * @return the count of matched elements.
   */
  default <F> int countR(F arg, NotNullBiPredicate<E, F> filter) {

    var array = array();
    var count = 0;

    for (int i = 0, length = size(); i < length; i++) {

      var element = array[i];

      if (filter.test(element, arg)) {
        count++;
      }
    }

    return count;
  }

  @Override
  default void forEach(Consumer<? super E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(array[i]);
    }
  }

  /**
   * Apply a function to each filtered element.
   *
   * @param filter the condition.
   * @param consumer the function.
   */
  default void forEachFiltered(NotNullPredicate<E> filter, NotNullConsumer<? super E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      E element = array[i];

      if (filter.test(element)) {
        consumer.accept(element);
      }
    }
  }

  /**
   * Apply a function to each element.
   *
   * @param argument the argument.
   * @param consumer the function.
   * @param <T> the type of an argument.
   */
  default <T> void forEach(T argument, NotNullBiConsumer<T, ? super E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(argument, array[i]);
    }
  }

  /**
   * Apply a function to each element.
   *
   * @param argument the argument.
   * @param consumer the function.
   * @param <T> the argument's type.
   */
  default <T> void forEachR(T argument, NotNullBiConsumer<? super E, T> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(array[i], argument);
    }
  }

  /**
   * Apply a function to each converted element.
   *
   * @param argument the argument.
   * @param converter the converter from E to C.
   * @param consumer the function.
   * @param <T> the argument's type.
   * @param <C> the converted type.
   */
  default <T, C> void forEachConverted(
      T argument,
      NotNullFunction<? super E, C> converter,
      NotNullBiConsumer<T, C> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(argument, converter.apply(array[i]));
    }
  }

  /**
   * Apply a function to each converted element.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param converter the converter from E to C.
   * @param consumer the function.
   * @param <F> the first argument's type.
   * @param <S> the second argument's type.
   * @param <C> the converted type.
   */
  default <F, S, C> void forEachConverted(
      F first,
      S second,
      NotNullFunction<? super E, C> converter,
      NotNullTripleConsumer<F, S, C> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(first, second, converter.apply(array[i]));
    }
  }

  /**
   * Apply a function to each element and converted argument.
   *
   * @param argument the argument.
   * @param converter the converter from T to C.
   * @param consumer the function.
   * @param <T> the argument's type.
   * @param <C> the converted type.
   * @since 9.8.0
   */
  default <T, C> void forEach(
      T argument,
      NotNullFunction<T, C> converter,
      NotNullBiConsumer<C, E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(converter.apply(argument), array[i]);
    }
  }

  /**
   * Apply a function to each filtered element.
   *
   * @param <T> the type of an argument.
   * @param argument the argument.
   * @param filter the condition.
   * @param consumer the function.
   */
  default <T> void forEachFiltered(
      T argument,
      NotNullBiPredicate<T, ? super E> filter,
      NotNullBiConsumer<T, ? super E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      E element = array[i];

      if (filter.test(argument, element)) {
        consumer.accept(argument, element);
      }
    }
  }

  /**
   * Apply a function to each element.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param consumer the function.
   * @param <F> the firs argument's type.
   * @param <S> the second argument's type.
   */
  default <F, S> void forEach(
      F first,
      S second,
      NotNullTripleConsumer<F, S, ? super E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(first, second, array[i]);
    }
  }

  /**
   * Apply a function to each element.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param consumer the function.
   * @param <A> the second argument's type.
   */
  default <A> void forEach(int first, A second, NotNullIntBiObjectConsumer<A, ? super E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(first, second, array[i]);
    }
  }

  /**
   * Apply the function to each element.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param consumer the function.
   * @param <F> the first argument type.
   * @param <S> the second argument type.
   */
  default <F, S> void forEachR(
      F first,
      S second,
      NotNullTripleConsumer<? super E, F, S> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(array[i], first, second);
    }
  }

  /**
   * Apply a function to each filtered element.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param filter the condition.
   * @param consumer the function.
   * @param <F> the firs argument's type.
   * @param <S> the second argument's type.
   */
  default <F, S> void forEachFiltered(
      F first,
      S second,
      NotNullTriplePredicate<F, S, ? super E> filter,
      NotNullTripleConsumer<F, S, ? super E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {

      E element = array[i];

      if (filter.test(first, second, element)) {
        consumer.accept(first, second, element);
      }
    }
  }

  /**
   * Apply a function to each element.
   *
   * @param first the first argument.
   * @param second the second argument.
   * @param consumer the function.
   * @param <F> the second argument's type.
   */
  default <F> void forEachL(
      long first,
      F second,
      NotNullLongBiObjectConsumer<F, ? super E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(first, second, array[i]);
    }
  }

  /**
   * Apply a function to each element.
   *
   * @param <F> the type parameter
   * @param first the first argument.
   * @param second the second argument.
   * @param consumer the function.
   */
  default <F> void forEachF(
      float first,
      F second,
      NotNullFloatBiObjectConsumer<F, ? super E> consumer) {

    var array = array();

    for (int i = 0, length = size(); i < length; i++) {
      consumer.accept(first, second, array[i]);
    }
  }
}
