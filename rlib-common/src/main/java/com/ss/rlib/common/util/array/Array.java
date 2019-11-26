package com.ss.rlib.common.util.array;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import static com.ss.rlib.common.util.ClassUtils.unsafeNNCast;
import com.ss.rlib.common.function.*;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.impl.DefaultArrayIterator;
import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Interface to implement dynamic arrays.
 * <p> To create to use {@link ArrayFactory}.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
public interface Array<E> extends Collection<E>, Serializable, Reusable, Cloneable, RandomAccess {

    /**
     * Create an empty read only array.
     *
     * @param <T> the result element's type.
     * @return the empty array.
     */
    static <T> @NotNull ReadOnlyArray<T> empty() {
        return unsafeNNCast(ArrayFactory.EMPTY_ARRAY);
    }

    /**
     * Create a new array for the element's type.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the new array.
     */
    static <T> @NotNull Array<T> ofType(@NotNull Class<? super T> type) {
        return ArrayFactory.newArray(type);
    }

    /**
     * Create a new array for the element's type.
     *
     * @param type     the element's type.
     * @param capacity the start capacity of this array.
     * @param <T>      the element's type.
     * @return the new array.
     */
    static <T> @NotNull Array<T> ofType(@NotNull Class<? super T> type, int capacity) {
        return ArrayFactory.newArray(type, capacity);
    }

    /**
     * Copy an array to a read only array.
     *
     * @param another the another array.
     * @param <T>     the element's type.
     * @return the new read only array.
     */
    static <T> @NotNull ReadOnlyArray<T> of(@NotNull Array<T> another) {
        return ArrayFactory.newReadOnlyArray(ArrayUtils.copyOfRange(another.array(), 0, another.size()));
    }

    /**
     * Creates a single element read only array.
     *
     * @param element the element.
     * @param <T>     the element's type.
     * @return the read only array.
     */
    static <T> @NotNull ReadOnlyArray<T> of(@NotNull T element) {

        T[] newArray = ArrayUtils.create(element.getClass(), 1);
        newArray[0] = element;

        return ArrayFactory.newReadOnlyArray(newArray);
    }

    @SafeVarargs
    static <T> @NotNull ReadOnlyArray<T> of(@NotNull T... elements) {
        return ArrayFactory.newReadOnlyArray(ArrayUtils.copyOf(elements));
    }

    @SafeVarargs
    static <T> @NotNull ReadOnlyArray<T> optionals(@NotNull Class<? super T> type, @NotNull Optional<T>... elements) {
        return ArrayFactory.newReadOnlyArray(Arrays.stream(elements)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toArray(value -> ArrayUtils.create(type, value)));
    }

    static <T, A extends Array<T>> @NotNull A append(@NotNull A first, @NotNull A second) {
        first.addAll(second);
        return first;
    }

    static <T> @NotNull ReadOnlyArray<T> combine(@NotNull Array<T> first, @NotNull Array<T> second) {

        var componentType = ClassUtils.<Class<T>>unsafeNNCast(first.array()
            .getClass()
            .getComponentType());

        var newArray = ArrayUtils.combine(first.toArray(componentType), second.toArray(componentType));

        return ArrayFactory.newReadOnlyArray(newArray);
    }

    /**
     * Create a supplier which creates new arrays.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the supplier.
     */
    static <T> @NotNull NotNullSupplier<Array<T>> supplier(@NotNull Class<? super T> type) {
        return () -> ArrayFactory.newConcurrentStampedLockArray(type);
    }

    /**
     * Create a function which creates new arrays.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the supplier.
     */
    static <T> @NotNull NotNullFunction<Class<? super T>, Array<T>> function(@NotNull Class<? super T> type) {
        return ArrayFactory::newConcurrentStampedLockArray;
    }

    /**
     * Copy all elements from this array to a target array.
     *
     * @param target the target array.
     */
    default void copyTo(@NotNull Array<? super E> target) {
        target.addAll(this);
    }

    /**
     * Adds all elements from the array to this array.
     *
     * @param array the array with new elements.
     * @return true if this array was changed.
     */
    boolean addAll(@NotNull Array<? extends E> array);

    /**
     * Adds all elements from the array to this array.
     *
     * @param array the array with new elements.
     * @return true if this array was changed.
     */
    boolean addAll(@NotNull E[] array);

    /**
     * Applies this function to each element of this array with replacing to result element from thia function.
     *
     * @param function the function.
     */
    default void apply(@NotNull Function<? super E, ? extends E> function) {

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
    E @NotNull [] array();

    @Override
    default @NotNull Stream<E> stream() {
        return Arrays.stream(array(), 0, size());
    }

    @Override
    default @NotNull Stream<E> parallelStream() {
        return stream().parallel();
    }

    @Override
    default boolean contains(@NotNull Object object) {

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
    default boolean containsAll(@NotNull Array<?> array) {

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
    default boolean containsAll(@NotNull Collection<?> array) {

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
    default boolean containsAll(@NotNull Object[] array) {

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
    @NotNull E fastRemove(int index);

    /**
     * Removes an element without saving original ordering of other elements.
     *
     * @param object the element to remove.
     * @return <code>true</code> if the element was removed.
     */
    default boolean fastRemove(@NotNull Object object) {

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
    default int fastRemoveAll(@NotNull E[] array) {

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
    @NotNull E get(int index);

    /**
     * Find an index of the object in this array.
     *
     * @param object the object to find.
     * @return the index of the object or -1.
     */
    default int indexOf(@NotNull Object object) {

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
    default @NotNull ArrayIterator<E> iterator() {
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
    default int lastIndexOf(@NotNull Object object) {

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
    default boolean removeAll(@NotNull Array<?> target) {

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
     * Removes all of this target's elements that are also contained in the specified array (optional operation)
     * with reordering.
     *
     * @param target array containing elements to be removed from this array.
     * @return true if this array changed as a result of the call.
     */
    default boolean fastRemoveAll(@NotNull Array<?> target) {

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
    default boolean removeAll(@NotNull Collection<?> target) {

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
    default boolean retainAll(@NotNull Array<?> target) {

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
    default boolean retainAll(@NotNull Collection<?> target) {

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
     * @param index   the element's index.
     * @param element the new element.
     */
    void replace(int index, @NotNull E element);

    /**
     * Removes the element at index.
     *
     * @param index the index of removing the element.
     * @return the removed element.
     */
    @NotNull E remove(int index);

    @Override
    default boolean remove(@NotNull Object object) {

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
    default @NotNull Array<E> sort(@NotNull ArrayComparator<E> comparator) {
        ArrayUtils.sort(array(), 0, size(), comparator);
        return this;
    }

    @Override
    default <T> @NotNull T[] toArray(@NotNull T[] newArray) {

        E[] array = array();

        if (newArray.length >= size()) {

            for (int i = 0, j = 0, length = array.length, newLength = newArray.length;
                 i < length && j < newLength; i++) {
                if (array[i] == null) continue;
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
     * @param <T>           the type parameter
     * @param componentType the type of the new array.
     * @return the copied array.
     */
    default <T> @NotNull T[] toArray(@NotNull Class<T> componentType) {

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
    default @NotNull UnsafeArray<E> asUnsafe() {
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
    default @NotNull E[] toArray() {
        var array = array();
        return Arrays.copyOf(array, size(), (Class<E[]>) array.getClass());
    }

    @NotNull String toString(@NotNull Function<E, @NotNull String> toString);

    @Override
    default boolean removeIf(@NotNull Predicate<@NotNull ? super E> filter) {

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
     * @param filter   the predicate which returns {@code true} for elements to be removed.
     * @param <A>      the argument's type.
     * @return {@code true} if any elements were removed.
     */
    default <A> boolean removeIf(@NotNull A argument, @NotNull NotNullBiPredicate<A, ? super E> filter) {

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
     * @param argument  the additional argument.
     * @param converter the converter of the argument.
     * @param filter    the predicate which returns {@code true} for elements to be removed.
     * @param <A>       the argument's type.
     * @param <B>       the argument converted type.
     * @return {@code true} if any elements were removed.
     */
    default <A, B> boolean removeIf(
        @NotNull A argument,
        @NotNull NotNullFunction<A, B> converter,
        @NotNull NotNullBiPredicate<B, ? super E> filter
    ) {

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
     * @param argument  the additional argument.
     * @param converter the converter of the elements.
     * @param filter    the predicate which returns {@code true} for elements to be removed.
     * @param <A>       the argument's type.
     * @param <B>       the element converted type.
     * @return {@code true} if any elements were removed.
     * @since 9.6.0
     */
    default <A, B> boolean removeIfConverted(
        @NotNull A argument,
        @NotNull NotNullFunction<? super E, B> converter,
        @NotNull NotNullBiPredicate<A, B> filter
    ) {

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
     * @param argument  the argument.
     * @param filter the condition.
     * @param <T>       the argument's type.
     * @return true if there is at least an element for the condition.
     */
    default <T> boolean anyMatch(@NotNull T argument, @NotNull NotNullBiPredicate<T, ? super E> filter) {
        return findAny(argument, filter) != null;
    }

    /**
     * Return true if there is at least a converted element for the condition.
     *
     * @param argument  the argument.
     * @param converter the converter element to another type.
     * @param filter    the condition.
     * @param <T>       the argument's type.
     * @param <C>       the converted element's type.
     * @return true if there is at least an element for the condition.
     * @since 9.7.0
     */
    default <T, C> boolean anyMatchConverted(
        @NotNull T argument,
        @NotNull NotNullFunction<? super E, C> converter,
        @NotNull NotNullBiPredicate<T, C> filter
    ) {
        return findAnyConverted(argument, converter, filter) != null;
    }

    /**
     * Return true if there is at least an element for the condition.
     *
     * @param argument the argument.
     * @param filter   the condition.
     * @return true if there is at least an element for the condition.
     */
    default boolean anyMatch(int argument, @NotNull NotNullIntObjectPredicate<? super E> filter) {
        return findAny(argument, filter) != null;
    }

    /**
     * Return true if there is at least an element for the condition.
     *
     * @param <T>      the argument's type.
     * @param argument the argument.
     * @param filter   the condition.
     * @return true if there is at least an element for the condition.
     */
    default <T> boolean anyMatchR(@NotNull T argument, @NotNull NotNullBiPredicate<? super E, T> filter) {
        return findAnyR(argument, filter) != null;
    }

    /**
     * Find an element using the condition.
     *
     * @param filter the condition.
     * @return the found element or null.
     */
    default @Nullable E findAny(@NotNull NotNullPredicate<E> filter) {

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
     * @param filter   the condition.
     * @param <T>      the argument's type.
     * @return the found element or null.
     */
    default <T> @Nullable E findAny(@NotNull T argument, @NotNull NotNullBiPredicate<T, ? super E> filter) {

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
     * @param argument  the argument.
     * @param converter the converted an element to another type.
     * @param filter    the condition.
     * @param <T>       the argument's type.
     * @param <C>       the converted element's type.
     * @return the found element or null.
     * @since 9.7.0
     */
    default <T, C> @Nullable E findAnyConverted(
        @NotNull T argument,
        @NotNull NotNullFunction<? super E, C> converter,
        @NotNull NotNullBiPredicate<T, C> filter
    ) {

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
     * @param <T>      the argument's type.
     * @param argument the argument.
     * @param filter   the condition.
     * @return the found element or null.
     */
    default <T> @Nullable E findAnyR(@NotNull T argument, @NotNull NotNullBiPredicate<? super E, T> filter) {

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
     * @param filter   the condition.
     * @return the found element or null.
     */
    default @Nullable E findAny(int argument, @NotNull NotNullIntObjectPredicate<? super E> filter) {

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
     * @param filter   the condition.
     * @return the found element or null.
     */
    default @Nullable E findAnyL(long argument, @NotNull NotNullLongObjectPredicate<E> filter) {

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
     * @param first  the first argument.
     * @param second the second argument.
     * @param filter the condition.
     * @param <F>    the first argument's type.
     * @param <S>    the second argument's type.
     * @return the found element or null.
     */
    default <F, S> @Nullable E findAny(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullTriplePredicate<F, S, E> filter
    ) {

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
     * @param argument  the argument.
     * @param converter the converter element to int.
     * @param filter    the condition.
     * @return the found element or null.
     * @since 9.6.0
     */
    default @Nullable E findAnyConvertedToInt(
        int argument,
        @NotNull NotNullFunctionInt<? super E> converter,
        @NotNull BiIntPredicate filter
    ) {

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
     * @param argument        the argument.
     * @param firstConverter  the converter element to T.
     * @param secondConverter the converter element to int.
     * @param filter          the condition.
     * @param <T>             the first converted type.
     * @return the found element or null.
     * @since 9.7.0
     */
    default <T> @Nullable E findAnyConvertedToInt(
        int argument,
        @NotNull NotNullFunction<? super E, T> firstConverter,
        @NotNull NotNullFunctionInt<T> secondConverter,
        @NotNull BiIntPredicate filter
    ) {

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
    default int count(@NotNull NotNullPredicate<E> filter) {

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
     * @param arg    the argument.
     * @param filter the condition.
     * @param <F>    the argument's type.
     * @return the count of matched elements.
     */
    default <F> int count(@NotNull F arg, @NotNull NotNullBiPredicate<F, E> filter) {

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
     * @param <F>    the argument's type.
     * @param arg    the argument.
     * @param filter the condition.
     * @return the count of matched elements.
     */
    default <F> int countR(@NotNull F arg, @NotNull NotNullBiPredicate<E, F> filter) {

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
    default void forEach(@NotNull Consumer<? super E> consumer) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(array[i]);
        }
    }

    /**
     * Apply a function to each filtered element.
     *
     * @param filter   the condition.
     * @param consumer the function.
     */
    default void forEachFiltered(@NotNull NotNullPredicate<E> filter, @NotNull NotNullConsumer<? super E> consumer) {

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
     * @param <T>      the type of an argument.
     */
    default <T> void forEach(@NotNull T argument, @NotNull NotNullBiConsumer<T, ? super E> consumer) {

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
     * @param <T>      the argument's type.
     */
    default <T> void forEachR(@NotNull T argument, @NotNull NotNullBiConsumer<? super E, T> consumer) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(array[i], argument);
        }
    }

    /**
     * Apply a function to each converted element.
     *
     * @param argument  the argument.
     * @param converter the converter from E to C.
     * @param consumer  the function.
     * @param <T>       the argument's type.
     * @param <C>       the converted type.
     */
    default <T, C> void forEachConverted(
        @NotNull T argument,
        @NotNull NotNullFunction<? super E, C> converter,
        @NotNull NotNullBiConsumer<T, C> consumer
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(argument, converter.apply(array[i]));
        }
    }

    /**
     * Apply a function to each converted element.
     *
     * @param first     the first argument.
     * @param second    the second argument.
     * @param converter the converter from E to C.
     * @param consumer  the function.
     * @param <F>       the first argument's type.
     * @param <S>       the second argument's type.
     * @param <C>       the converted type.
     */
    default <F, S, C> void forEachConverted(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullFunction<? super E, C> converter,
        @NotNull NotNullTripleConsumer<F, S, C> consumer
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(first, second, converter.apply(array[i]));
        }
    }

    /**
     * Apply a function to each element and converted argument.
     *
     * @param argument  the argument.
     * @param converter the converter from T to C.
     * @param consumer  the function.
     * @param <T>       the argument's type.
     * @param <C>       the converted type.
     * @since 9.8.0
     */
    default <T, C> void forEach(
        @NotNull T argument,
        @NotNull NotNullFunction<T, C> converter,
        @NotNull NotNullBiConsumer<C, E> consumer
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(converter.apply(argument), array[i]);
        }
    }

    /**
     * Apply a function to each filtered element.
     *
     * @param <T>      the type of an argument.
     * @param argument the argument.
     * @param filter   the condition.
     * @param consumer the function.
     */
    default <T> void forEachFiltered(
        @NotNull T argument,
        @NotNull NotNullBiPredicate<T, ? super E> filter,
        @NotNull NotNullBiConsumer<T, ? super E> consumer
    ) {

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
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function.
     * @param <F>      the firs argument's type.
     * @param <S>      the second argument's type.
     */
    default <F, S> void forEach(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullTripleConsumer<F, S, ? super E> consumer
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(first, second, array[i]);
        }
    }

    /**
     * Apply a function to each element.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function.
     * @param <A>      the second argument's type.
     */
    default <A> void forEach(
        int first,
        @NotNull A second,
        @NotNull NotNullIntBiObjectConsumer<A, ? super E> consumer
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(first, second, array[i]);
        }
    }

    /**
     * Apply the function to each element.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function.
     * @param <F>      the first argument type.
     * @param <S>      the second argument type.
     */
    default <F, S> void forEachR(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullTripleConsumer<? super E, F, S> consumer
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(array[i], first, second);
        }
    }

    /**
     * Apply a function to each filtered element.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param filter   the condition.
     * @param consumer the function.
     * @param <F>      the firs argument's type.
     * @param <S>      the second argument's type.
     */
    default <F, S> void forEachFiltered(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullTriplePredicate<F, S, ? super E> filter,
        @NotNull NotNullTripleConsumer<F, S, ? super E> consumer
    ) {

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
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function.
     * @param <F>      the second argument's type.
     */
    default <F> void forEachL(
        long first,
        @NotNull F second,
        @NotNull NotNullLongBiObjectConsumer<F, ? super E> consumer
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(first, second, array[i]);
        }
    }

    /**
     * Apply a function to each element.
     *
     * @param <F>      the type parameter
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function.
     */
    default <F> void forEachF(
        float first,
        @NotNull F second,
        @NotNull NotNullFloatBiObjectConsumer<F, ? super E> consumer
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            consumer.accept(first, second, array[i]);
        }
    }
}
