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
import java.util.function.*;
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
     * Wrap a native array to a read only array.
     *
     * @param elements the elements to wrap.
     * @param <T> the element's type.
     * @return the new read only array.
     */
    static <T> @NotNull ReadOnlyArray<T> of(@NotNull T[] elements) {
        return ArrayFactory.newReadOnlyArray(ArrayUtils.copyOf(elements));
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
    static <T> @NotNull ReadOnlyArray<T> of(@NotNull T element, @NotNull T... elements) {

        T[] newArray = ArrayUtils.copyOf(elements, 1, 1);
        newArray[0] = element;

        return ArrayFactory.newReadOnlyArray(newArray);
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
    static <T> @NotNull Supplier<Array<T>> supplier(@NotNull Class<? super T> type) {
        return () -> ArrayFactory.newConcurrentStampedLockArray(type);
    }

    /**
     * Create a function which creates new arrays.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the supplier.
     */
    static <T> @NotNull Function<Class<? super T>, Array<T>> function(@NotNull Class<? super T> type) {
        return ArrayFactory::newConcurrentStampedLockArray;
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
     * Returns <tt>true</tt> if this array contains all of the elements in the specified array.
     *
     * @param array the array to be checked for containment in this array
     * @return <tt>true</tt> if this array contains all of the elements in the specified array.
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
     * Returns <tt>true</tt> if this array contains all of the elements in the specified array.
     *
     * @param array the array to be checked for containment in this array
     * @return <tt>true</tt> if this array contains all of the elements in the specified array.
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
    default int fastRemove(@NotNull Array<? extends E> array) {

        int count = 0;

        for (E object : array.array()) {
            if (object == null) break;
            if (fastRemove(object)) count++;
        }

        return count;
    }

    /**
     * Removes the each element from the array.
     *
     * @param array the array with elements to remove.
     * @return count of removed elements.
     */
    default int fastRemove(@NotNull E[] array) {

        int count = 0;

        for (E object : array) {
            if (object == null) break;
            if (fastRemove(object)) count++;
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

    @Override
    default void forEach(@NotNull Consumer<? super E> consumer) {

        for (E element : array()) {

            if (element == null) {
                break;
            }

            consumer.accept(element);
        }
    }

    /**
     * Apply the function to each filtered element.
     *
     * @param condition the condition.
     * @param function  the function.
     */
    default void forEach(@NotNull Predicate<E> condition, @NotNull Consumer<? super E> function) {

        for (E element : array()) {

            if (element == null) {
                break;
            }

            if (condition.test(element)) {
                function.accept(element);
            }
        }
    }

    /**
     * Apply the function to each element.
     *
     * @param <T>      the type of an argument.
     * @param argument the argument.
     * @param function the function.
     */
    default <T> void forEach(@NotNull T argument, @NotNull NotNullBiConsumer<? super E, T> function) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            function.accept(array[i], argument);
        }
    }

    /**
     * Apply the function to each element.
     *
     * @param argument the argument.
     * @param function the function.
     * @param <T>      the argument's type.
     */
    default <T> void forEachR(@NotNull T argument, @NotNull BiConsumer<@NotNull T, @NotNull E> function) {

        for (E element : array()) {

            if (element == null) {
                break;
            }

            function.accept(argument, element);
        }
    }

    /**
     * Apply a function to each converted element.
     *
     * @param argument  the argument.
     * @param converter the converter from T to C.
     * @param function  the function.
     * @param <T>       the argument's type.
     * @param <C>       the converted type.
     */
    default <T, C> void forEachConverted(
        @NotNull T argument,
        @NotNull NotNullFunction<? super E, C> converter,
        @NotNull NotNullBiConsumer<C, T> function
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            function.accept(converter.apply(array[i]), argument);
        }
    }

    /**
     * Apply a function to each element and converted argument.
     *
     * @param argument  the argument.
     * @param converter the converter from T to C.
     * @param function  the function.
     * @param <T>       the argument's type.
     * @param <C>       the converted type.
     */
    default <T, C> void forEach(
        @NotNull T argument,
        @NotNull NotNullFunction<T, C> converter,
        @NotNull NotNullBiConsumer<C, E> function
    ) {

        var array = array();

        for (int i = 0, length = size(); i < length; i++) {
            function.accept(converter.apply(argument), array[i]);
        }
    }

    /**
     * Apply the function to each filtered element.
     *
     * @param <T>       the type of an argument.
     * @param argument  the argument.
     * @param condition the condition.
     * @param function  the function.
     */
    default <T> void forEach(
            @Nullable T argument,
            @NotNull BiPredicate<E, T> condition,
            @NotNull BiConsumer<E, T> function
    ) {

        for (E element : array()) {

            if (element == null) {
                break;
            }

            if (condition.test(element, argument)) {
                function.accept(element, argument);
            }
        }
    }

    /**
     * Apply the function to each element.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     * @param <F>      the firs argument's type.
     * @param <S>      the second argument's type.
     */
    default <F, S> void forEach(@Nullable F first, @Nullable S second, @NotNull TripleConsumer<E, F, S> function) {

        for (E element : array()) {

            if (element == null) {
                break;
            }

            function.accept(element, first, second);
        }
    }

    /**
     * Apply the function to each element.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     * @param <F>      the firs argument's type.
     * @param <S>      the second argument's type.
     */
    default <F, S> void forEachRm(
            @NotNull F first,
            @NotNull S second,
            @NotNull TripleConsumer<@NotNull F, @NotNull E, @NotNull S> function
    ) {

        for (E element : array()) {

            if (element == null) {
                break;
            }

            function.accept(first, element, second);
        }
    }

    /**
     * Apply the function to each filtered element.
     *
     * @param <F>       the type parameter
     * @param <S>       the type parameter
     * @param first     the first argument.
     * @param second    the second argument.
     * @param condition the condition.
     * @param function  the function.
     */
    default <F, S> void forEach(
            @Nullable F first,
            @Nullable S second,
            @NotNull TriplePredicate<E, F, S> condition,
            @NotNull TripleConsumer<E, F, S> function
    ) {

        for (E element : array()) {

            if (element == null) {
                break;
            }

            if (condition.test(element, first, second)) {
                function.accept(element, first, second);
            }
        }
    }

    /**
     * Apply the function to each element.
     *
     * @param <F>      the type parameter
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     */
    default <F> void forEachL(
            long first,
            @Nullable F second,
            @NotNull ObjectLongObjectConsumer<@NotNull E, F> function
    ) {

        for (E element : array()) {

            if (element == null) {
                break;
            }

            function.accept(element, first, second);
        }
    }

    /**
     * Apply the function to each element.
     *
     * @param <F>      the type parameter
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     */
    default <F> void forEachF(
            float first,
            @Nullable F second,
            @NotNull ObjectFloatObjectConsumer<@NotNull E, F> function
    ) {

        for (E element : array()) {

            if (element == null) {
                break;
            }

            function.accept(element, first, second);
        }
    }

    /**
     * Calculate a count of matched elements.
     *
     * @param <F>       the argument's type.
     * @param arg       the argument.
     * @param predicate the condition.
     * @return the count of matched elements.
     */
    default <F> int count(@NotNull F arg, @NotNull BiPredicate<E, F> predicate) {

        int count = 0;

        for (var element : array()) {

            if (element == null) {
                break;
            } else if (predicate.test(element, arg)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Calculate a count of matched elements with reserved ordering of arguments.
     *
     * @param <F>       the argument's type.
     * @param arg       the argument.
     * @param predicate the condition.
     * @return the count of matched elements.
     */
    default <F> int countR(@NotNull F arg, @NotNull BiPredicate<F, E> predicate) {

        int count = 0;

        for (var element : array()) {

            if (element == null) {
                break;
            } else if (predicate.test(arg, element)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Gets the element by the index.
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
        return isEmpty() ? null : slowRemove(0);
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
     * Removes all of this target's elements that are also contained in the specified array (optional operation).  After
     * this call returns, this array will contain no elements in common with the specified array.
     *
     * @param target array containing elements to be removed from this array.
     * @return <tt>true</tt> if this array changed as a result of the call.
     */
    default boolean removeAll(@NotNull Array<?> target) {

        if (target.isEmpty()) {
            return false;
        }

        int count = 0;

        for (Object element : target.array()) {
            if (element == null) {
                break;
            } else if (slowRemove(element)) {
                count++;
            }
        }

        return count == target.size();
    }

    @Override
    default boolean removeAll(@NotNull Collection<?> target) {

        if (target.isEmpty()) {
            return false;
        }

        int count = 0;

        for (Object element : target) {
            if (element == null) {
                break;
            } else if (slowRemove(element)) {
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
     * @return <tt>true</tt> if this array changed as a result of the call.
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
     * Find an element using the condition.
     *
     * @param predicate the condition.
     * @return the found element or null.
     */
    default @Nullable E findAny(@NotNull Predicate<E> predicate) {

        if (isEmpty()) {
            return null;
        }

        for (E element : array()) {
            if (element == null) {
                break;
            } else if (predicate.test(element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Search an element using the condition.
     *
     * @param <T>       the argument's type.
     * @param argument  the argument.
     * @param predicate the condition.
     * @return the found element or null.
     */
    default <T> @Nullable E findAny(@Nullable T argument, @NotNull BiPredicate<? super E, T> predicate) {

        if (isEmpty()) {
            return null;
        }

        for (E element : array()) {
            if (element == null) {
                break;
            } else if (predicate.test(element, argument)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Return true if there is at least an element for the condition.
     *
     * @param <T>       the argument's type.
     * @param argument  the argument.
     * @param predicate the condition.
     * @return true if there is at least an element for the condition.
     */
    default <T> boolean anyMatch(@Nullable T argument, @NotNull BiPredicate<? super E, T> predicate) {
        return findAny(argument, predicate) != null;
    }

    /**
     * Find an element for the condition.
     *
     * @param <T>       the argument's type.
     * @param argument  the argument.
     * @param condition the condition.
     * @return the found element or null.
     */
    default <T> @Nullable E findAnyR(@Nullable T argument, @NotNull BiPredicate<T, ? super E> condition) {

        if (isEmpty()) {
            return null;
        }

        for (E element : array()) {
            if (element == null) {
                break;
            } else if (condition.test(argument, element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Return true if there is at least an element for the condition.
     *
     * @param <T>       the argument's type.
     * @param argument  the argument.
     * @param condition the condition.
     * @return true if there is at least an element for the condition.
     */
    default <T> boolean anyMatchR(@Nullable T argument, @NotNull BiPredicate<T, ? super E> condition) {
        return findAnyR(argument, condition) != null;
    }

    /**
     * Search an element using the condition.
     *
     * @param <F>       the first argument's type.
     * @param <S>       the second argument's type.
     * @param first     the first argument.
     * @param second    the second argument.
     * @param predicate the condition.
     * @return the found element or null.
     */
    default <F, S> @Nullable E findAny(
        @Nullable F first,
        @Nullable S second,
        @NotNull TriplePredicate<E, F, S> predicate
    ) {

        if (isEmpty()) {
            return null;
        }

        for (E element : array()) {
            if (element == null) {
                break;
            } else if (predicate.test(element, first, second)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Search an element using the condition.
     *
     * @param argument  the argument.
     * @param predicate the condition.
     * @return the found element or null.
     */
    default @Nullable E findAny(int argument, @NotNull ObjectIntPredicate<E> predicate) {

        if (isEmpty()) {
            return null;
        }

        for (E element : array()) {
            if (element == null) {
                break;
            } else if (predicate.test(element, argument)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Search an element using the condition.
     *
     * @param argument  the argument.
     * @param predicate the condition.
     * @return the found element or null.
     */
    default @Nullable E findAnyL(long argument, @NotNull ObjectLongPredicate<E> predicate) {

        if (isEmpty()) {
            return null;
        }

        for (E element : array()) {
            if (element == null) {
                break;
            } else if (predicate.test(element, argument)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Set the element by the index.
     *
     * @param index   the element's index.
     * @param element the new element.
     */
    void set(int index, @NotNull E element);

    /**
     * Removes the element at index without reordering.
     *
     * @param index the index of removing the element.
     * @return the removed element.
     * @see Array#remove(int) 
     */
    @Deprecated(forRemoval = true)
    default @NotNull E slowRemove(int index) {
        return remove(index);
    }

    /**
     * Removes the element at index.
     *
     * @param index the index of removing the element.
     * @return the removed element.
     */
    @NotNull E remove(int index);

    @Override
    default boolean remove(@NotNull Object object) {

        int index = indexOf(object);

        if (index >= 0) {
            remove(index);
        }

        return index >= 0;
    }

    /**
     * Remove the element without reordering.
     *
     * @param object the element.
     * @return true if the element was removed.
     * @see Array#remove(Object)
     */
    @Deprecated(forRemoval = true)
    default boolean slowRemove(@NotNull Object object) {
        return remove(object);
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
        E[] array = array();
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
}
