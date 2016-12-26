package rlib.util.array;

import static rlib.util.ClassUtils.unsafeCast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import rlib.function.ObjectIntPredicate;
import rlib.function.ObjectLongObjectConsumer;
import rlib.function.ObjectLongPredicate;
import rlib.function.TripleConsumer;
import rlib.function.TriplePredicate;
import rlib.util.ArrayUtils;
import rlib.util.pools.Reusable;

/**
 * Interface to implement dynamic arrays. Main advantages compared to an ArrayList, the ability to
 * iterate in the fastest way possible and without prejudice to GC: <p>
 * <pre>
 * for(? element : array.array()) {
 *
 * 	if(element == null)	{
 * 		break;
 *    }
 *
 * 	// handle element
 * }
 * </pre>
 * <p> To create to use {@link ArrayFactory}.
 *
 * @author JavaSaBr
 */
public interface Array<E> extends Iterable<E>, Serializable, Reusable {

    /**
     * Adds a new element to this array.
     *
     * @param object the new element.
     * @return this array.
     */
    @NotNull
    Array<E> add(@NotNull E object);

    /**
     * Adds all elements from the array to this array.
     *
     * @param array the array with new elements.
     * @return this array.
     */
    @NotNull
    Array<E> addAll(@NotNull Array<? extends E> array);

    /**
     * Adds all elements from the array to this array.
     *
     * @param array the array with new elements.
     * @return this array.
     */
    @NotNull
    Array<E> addAll(@NotNull E[] array);


    /**
     * Adds all elements from the collection to this array.
     *
     * @param collection the collection with new elements.
     * @return this array.
     */
    @NotNull
    Array<E> addAll(@NotNull Collection<? extends E> collection);

    /**
     * Applies this function to each element of this array with replacing to result element from
     * thia function.
     *
     * @param function the function.
     */
    default void apply(@NotNull final Function<? super E, ? extends E> function) {
        final E[] array = array();
        for (int i = 0, length = size(); i < length; i++) {
            array[i] = function.apply(array[i]);
        }
    }

    /**
     * @return the wrapped array.
     */
    @NotNull
    E[] array();

    /**
     * @return the new string from this array.
     */
    @NotNull
    default Stream<E> stream() {
        return Arrays.stream(array(), 0, size());
    }

    /**
     * Cleans this array.
     *
     * @return this array.
     */
    @NotNull
    Array<E> clear();

    /**
     * Checks exists of the element in this array.
     *
     * @param object the element.
     * @return false if this object is null or doesn't exists in this array.
     */
    default boolean contains(@NotNull final Object object) {
        for (final E element : array()) {
            if (element == null) break;
            if (element.equals(object)) return true;
        }
        return false;
    }

    /**
     * Returns <tt>true</tt> if this array contains all of the elements in the specified array.
     *
     * @param array the array to be checked for containment in this array
     * @return <tt>true</tt> if this array contains all of the elements in the specified array.
     */
    default boolean containsAll(@NotNull final Array<?> array) {
        if (array.isEmpty()) return false;

        for (final Object element : array.array()) {
            if (element == null) break;
            if (!contains(element)) return false;
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if this array contains all of the elements in the specified array.
     *
     * @param array the array to be checked for containment in this array
     * @return <tt>true</tt> if this array contains all of the elements in the specified array.
     */
    default boolean containsAll(@NotNull final Object[] array) {
        if (array.length < 1) return false;

        for (final Object element : array) {
            if (!contains(element)) return false;
        }

        return true;
    }

    @Override
    default void free() {
        clear();
    }

    /**
     * Removes the element at index with reordering.
     *
     * @param index the index for removing the element.
     * @return the removed element.
     */
    @NotNull
    E fastRemove(int index);

    /**
     * Removes the specified element with reordering.
     *
     * @param object the element for removing.
     * @return <code>true</code> if the element was removed.
     */
    default boolean fastRemove(@NotNull final Object object) {
        final int index = indexOf(object);
        if (index == -1) return false;
        fastRemove(index);
        return true;
    }

    /**
     * Removes the each element from the array.
     *
     * @param array the array with elements to remove.
     * @return count of removed elements.
     */
    default int fastRemove(@NotNull final Array<? extends E> array) {

        int count = 0;

        for (final E object : array.array()) {
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
    default int fastRemove(@NotNull final E[] array) {

        int count = 0;

        for (final E object : array) {
            if (object == null) break;
            if (fastRemove(object)) count++;
        }

        return count;
    }

    /**
     * @return the first element of this array or null.
     */
    default E first() {
        if (isEmpty()) return null;
        return get(0);
    }

    @Override
    default void forEach(@NotNull final Consumer<? super E> consumer) {
        for (final E element : array()) {
            if (element == null) break;
            consumer.accept(element);
        }
    }

    /**
     * Итерирование массива.
     *
     * @param predicate фильтр элементов.
     * @param consumer  функция для обработки элементов.
     */
    default void forEach(@NotNull final Predicate<E> predicate, @NotNull final Consumer<? super E> consumer) {
        for (final E element : array()) {
            if (element == null) break;
            if (predicate.test(element)) consumer.accept(element);
        }
    }

    /**
     * Итерирование массива с дополнительным аргументом.
     *
     * @param argument дополнительный аргумент.
     * @param consumer функция для обработки элементов.
     */
    default <T> void forEach(@Nullable final T argument, @NotNull final BiConsumer<E, T> consumer) {
        for (final E element : array()) {
            if (element == null) break;
            consumer.accept(element, argument);
        }
    }

    /**
     * Итерирование массива с дополнительным аргументом.
     *
     * @param argument  дополнительный аргумент.
     * @param predicate фильтр элементов.
     * @param consumer  функция для обработки элементов.
     */
    default <T> void forEach(@Nullable final T argument, @NotNull final BiPredicate<E, T> predicate, @NotNull final BiConsumer<E, T> consumer) {
        for (final E element : array()) {
            if (element == null) break;
            if (predicate.test(element, argument)) consumer.accept(element, argument);
        }
    }

    /**
     * Итерирование массива с дополнительным аргументом.
     *
     * @param first     первый аргумент.
     * @param second    второй аргумент.
     * @param predicate фильтр элементов.
     * @param consumer  функция для обработки элементов.
     */
    default <F, S> void forEach(@Nullable final F first, @Nullable final S second, @NotNull final TriplePredicate<E, F, S> predicate, @NotNull final TripleConsumer<E, F, S> consumer) {
        for (final E element : array()) {
            if (element == null) break;
            if (predicate.test(element, first, second)) consumer.accept(element, first, second);
        }
    }

    /**
     * Итерирование массива с двумя дополнительными аргументомами.
     *
     * @param first    первый дополнительный аргумент.
     * @param second   второй дополнительный аргумент.
     * @param consumer функция для обработки элементов.
     */
    default <F, S> void forEach(@Nullable final F first, @Nullable final S second, @NotNull final TripleConsumer<E, F, S> consumer) {
        for (final E element : array()) {
            if (element == null) break;
            consumer.accept(element, first, second);
        }
    }

    /**
     * Итерирование массива с двумя дополнительными аргументомами.
     *
     * @param first    первый дополнительный аргумент.
     * @param second   второй дополнительный аргумент.
     * @param consumer функция для обработки элементов.
     */
    default <F> void forEach(final long first, @Nullable final F second, @NotNull final ObjectLongObjectConsumer<E, F> consumer) {
        for (final E element : array()) {
            if (element == null) break;
            consumer.accept(element, first, second);
        }
    }

    /**
     * Gets the element for the index.
     *
     * @param index the index of an element.
     * @return the element for the index.
     */
    @NotNull
    E get(int index);

    /**
     * Finds the index of the object in this array.
     *
     * @param object the object to find.
     * @return the index of the objet or -1.
     */
    default int indexOf(@NotNull final Object object) {

        int index = 0;

        for (final E element : array()) {
            if (element == null) break;
            if (Objects.equals(object, element)) return index;
            index++;
        }

        return -1;
    }

    /**
     * @return true is this array is empty.
     */
    default boolean isEmpty() {
        return size() < 1;
    }

    @Override
    ArrayIterator<E> iterator();

    /**
     * @return the last element of this array or null.
     */
    @Nullable
    default E last() {
        final int size = size();
        if (size < 1) return null;
        return get(size - 1);
    }

    /**
     * Finds the last index for the object in this array.
     *
     * @param object the object.
     * @return the last index of the object in this array or -1.
     */
    default int lastIndexOf(@NotNull final Object object) {

        final E[] array = array();
        int last = -1;

        for (int i = 0, length = size(); i < length; i++) {
            final E element = array[i];
            if (element.equals(object)) last = i;
        }

        return last;
    }

    /**
     * Gets and removes the first element from this array.
     *
     * @return the first element or null.
     */
    @Nullable
    default E poll() {
        if (isEmpty()) return null;
        return slowRemove(0);
    }

    /**
     * Gets and removes the last element of this array.
     *
     * @return the last element or null.
     */
    @Nullable
    default E pop() {
        if (isEmpty()) return null;
        return fastRemove(size() - 1);
    }

    /**
     * Removes all of this target's elements that are also contained in the specified array
     * (optional operation).  After this call returns, this array will contain no elements in common
     * with the specified array.
     *
     * @param target array containing elements to be removed from this array.
     * @return <tt>true</tt> if this array changed as a result of the call.
     */
    default boolean removeAll(@NotNull final Array<?> target) {
        if (target.isEmpty()) return false;

        int count = 0;

        for (final Object element : target.array()) {
            if (element == null) break;
            if (fastRemove(element)) count++;
        }

        return count == target.size();
    }

    /**
     * Retains only the elements in this array that are contained in the specified array (optional
     * operation).  In other words, removes from this array all of its elements that are not
     * contained in the specified array.
     *
     * @param target array containing elements to be retained in this array.
     * @return <tt>true</tt> if this array changed as a result of the call.
     */
    default boolean retainAll(@NotNull final Array<?> target) {

        final E[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (!target.contains(array[i])) {
                fastRemove(i--);
                length--;
            }
        }

        return true;
    }

    /**
     * Ищет первый элемент удовлетворяющий условию.
     *
     * @param predicate условия отбора элемента.
     * @return искомый объект либо null.
     */
    @Nullable
    default E search(@NotNull final Predicate<E> predicate) {
        for (final E element : array()) {
            if (element == null) break;
            if (predicate.test(element)) return element;
        }
        return null;
    }

    /**
     * Ищет первый элемент удовлетворяющий условию c дополнительным аргументом.
     *
     * @param argument  дополнительный аргумент.
     * @param predicate условия отбора элемента.
     * @return искомый объект либо null.
     */
    @Nullable
    default <T> E search(@Nullable final T argument, @NotNull final BiPredicate<E, T> predicate) {
        for (final E element : array()) {
            if (element == null) break;
            if (predicate.test(element, argument)) return element;
        }
        return null;
    }

    /**
     * Ищет первый элемент удовлетворяющий условию c дополнительным аргументом.
     *
     * @param argument  дополнительный аргумент.
     * @param predicate условия отбора элемента.
     * @return искомый объект либо null.
     */
    @Nullable
    default E search(final int argument, @NotNull final ObjectIntPredicate<E> predicate) {
        for (final E element : array()) {
            if (element == null) break;
            if (predicate.test(element, argument)) return element;
        }
        return null;
    }

    /**
     * Ищет первый элемент удовлетворяющий условию c дополнительным аргументом.
     *
     * @param argument  дополнительный аргумент.
     * @param predicate условия отбора элемента.
     * @return искомый объект либо null.
     */
    @Nullable
    default E searchL(final long argument, @NotNull final ObjectLongPredicate<E> predicate) {
        for (final E element : array()) {
            if (element == null) break;
            if (predicate.test(element, argument)) return element;
        }
        return null;
    }

    /**
     * Set the element for the index.
     *
     * @param index   индекс, по которому нужно устоновить элемент.
     * @param element элемент, который нужно добавит в массив.
     */
    void set(int index, @NotNull E element);

    /**
     * @return the count of elements in this {@link Array}.
     */
    int size();

    /**
     * Removes the element for the index without reordering.
     *
     * @param index the index of the element.
     * @return the removed element.
     */
    @NotNull
    E slowRemove(int index);

    /**
     * Removes the element without reordering.
     *
     * @param object the element.
     * @return true if the element was removed.
     */
    boolean slowRemove(@NotNull Object object);

    /**
     * Sorts this array using the comparator.
     *
     * @param comparator the comparator.
     */
    @NotNull
    default Array<E> sort(@NotNull final ArrayComparator<E> comparator) {
        ArrayUtils.sort(array(), comparator);
        return this;
    }

    /**
     * Copies this array to the array.
     *
     * @param newArray the new array.
     */
    @NotNull
    default <T extends E> T[] toArray(@NotNull final T[] newArray) {

        final E[] array = array();

        if (newArray.length >= size()) {

            for (int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {
                if (array[i] == null) continue;
                newArray[j++] = unsafeCast(array[i]);
            }

            return newArray;
        }

        final Class<T[]> arrayClass = unsafeCast(newArray.getClass());
        final Class<T> componentType = unsafeCast(arrayClass.getComponentType());

        return toArray(componentType);
    }

    /**
     * Copies this array to the new array.
     *
     * @param componentType the type of the new array.
     */
    @NotNull
    default <T extends E> T[] toArray(@NotNull final Class<T> componentType) {

        final T[] newArray = ArrayUtils.create(componentType, size());
        final E[] array = array();

        System.arraycopy(array, 0, newArray, 0, size());

        return newArray;
    }

    default UnsafeArray<E> asUnsafe() {
        if (this instanceof UnsafeArray) return (UnsafeArray<E>) this;
        throw new UnsupportedOperationException();
    }
}
