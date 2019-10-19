package com.ss.rlib.common.util.array;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.impl.*;
import org.jetbrains.annotations.NotNull;

/**
 * The factory to create different kinds of arrays.
 *
 * @author JavaSaBr
 */
public class ArrayFactory {

    public static final Array<?> EMPTY_ARRAY = newReadOnlyArray(ArrayUtils.EMPTY_OBJECT_ARRAY);

    /**
     * Wrap arguments as an array collection..
     *
     * @param <E>  the argument's type.
     * @param args the elements to be wrapped.
     * @return the new array collection with the wrapped arguments.
     */
    @SafeVarargs
    public static <E> Array<E> asArray(@NotNull E... args) {
        return new FastArray<>(args);
    }

    /**
     * Create the new array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> Array<E> newArray(@NotNull Class<? super E> type) {
        return newUnsafeArray(type);
    }

    /**
     * Create the new copy on modify array.
     *
     * @param <E>  the element's type.
     * @param type the element's type.
     * @return the new array.
     */
    public static <E> Array<E> newCopyOnModifyArray(@NotNull Class<? super E> type) {
        return new CopyOnModifyArray<>(type, 0);
    }

    /**
     * Create the new unsafe array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new unsafe array.
     */
    public static <E> UnsafeArray<E> newUnsafeArray(@NotNull Class<? super E> type) {
        return new FastArray<>(type);
    }

    /**
     * Create the new array.
     *
     * @param <E>      the type parameter
     * @param type     the type of the array.
     * @param capacity the init size of the array.
     * @return the new array.
     */
    public static <E> Array<E> newArray(@NotNull Class<? super E> type, int capacity) {
        return newUnsafeArray(type, capacity);
    }

    /**
     * Create a new read only array.
     *
     * @param <E> the element's type.
     * @return the new read only array.
     */
    public static <E> ReadOnlyArray<E> newReadOnlyArray(@NotNull E[] elements) {
        return new ReadOnlyFastArray<>(elements);
    }

    /**
     * Creates the new unsafe array.
     *
     * @param <E>      the type parameter
     * @param type     the type of the array.
     * @param capacity the init size of the array.
     * @return the new unsafe array.
     */
    public static <E> UnsafeArray<E> newUnsafeArray(@NotNull Class<? super E> type, int capacity) {
        return new FastArray<>(type, capacity);
    }

    /**
     * Create the new array set.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> Array<E> newArraySet(@NotNull Class<? super E> type) {
        return new FastArraySet<>(type);
    }

    /**
     * Create the new concurrent array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentReentrantRWLockArray(@NotNull Class<? super E> type) {
        return new ConcurrentReentrantRWLockArray<>(type);
    }

    /**
     * Create the new concurrent array set.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentReentrantRWLockArraySet(@NotNull Class<? super E> type) {
        return new ConcurrentReentrantRWLockArraySet<>(type);
    }

    /**
     * Create the new concurrent array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentAtomicARSWLockArray(@NotNull Class<? super E> type) {
        return new ConcurrentAtomicARSWLockArray<>(type);
    }

    /**
     * Create the new concurrent array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentStampedLockArray(@NotNull Class<? super E> type) {
        return new ConcurrentStampedLockArray<>(type);
    }

    /**
     * Create the new sorted array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E extends Comparable<E>> Array<E> newSortedArray(@NotNull Class<? super E> type) {
        return new SortedArray<>(unsafeCast(type));
    }

    /**
     * Create the new synchronized array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> Array<E> newSynchronizedArray(@NotNull Class<? super E> type) {
        return new SynchronizedArray<>(type);
    }

    public static @NotNull MutableIntegerArray newMutableIntegerArray() {
        return new DefaultIntegerArray();
    }

    public static @NotNull MutableIntegerArray newMutableIntegerArray(int capacity) {
        return new DefaultIntegerArray(capacity);
    }

    public static @NotNull MutableIntegerArray newMutableIntegerArray(int... numbers) {
        return new DefaultIntegerArray(numbers);
    }

    /**
     * Create the a long array.
     *
     * @return the new array.
     */
    public static @NotNull LongArray newLongArray() {
        return new FastLongArray();
    }

    /**
     * Create the a long array.
     *
     * @param capacity the init size of the array.
     * @return the new array.
     */
    public static @NotNull LongArray newLongArray(int capacity) {
        return new FastLongArray(capacity);
    }

    /**
     * Create a new float array.
     *
     * @param elements the elements of the new float array.
     * @return the new float array.
     * @since 8.1.0
     */
    public static @NotNull float[] toFloatArray(float... elements) {
        return elements;
    }

    /**
     * Create a new int array.
     *
     * @param elements the elements of the new int array.
     * @return the new int array.
     * @since 8.1.0
     */
    public static @NotNull int[] toIntArray(int... elements) {
        return elements;
    }

    /**
     * Create a new long array.
     *
     * @param elements the elements of the new long array.
     * @return the new long array.
     * @since 9.0.3
     */
    public static @NotNull long[] toLongArray(long... elements) {
        return elements;
    }

    /**
     * Create a new boolean array.
     *
     * @param elements the elements of the new boolean array.
     * @return the new boolean array.
     * @since 9.2.1
     */
    public static @NotNull boolean[] toBooleanArray(boolean... elements) {
        return elements;
    }

    /**
     * Create a new object array.
     *
     * @param <T>      the base array's element type.
     * @param <K>      the array's element type.
     * @param elements the elements of the new array.
     * @return the new array.
     */
    @SafeVarargs
    public static <T, K extends T> @NotNull T[] toArray(K... elements) {
        return elements;
    }
}
