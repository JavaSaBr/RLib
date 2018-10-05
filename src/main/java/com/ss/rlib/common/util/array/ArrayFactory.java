package com.ss.rlib.common.util.array;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.impl.*;
import org.jetbrains.annotations.NotNull;

/**
 * The factory for creating arrays.
 *
 * @author JavaSaBr
 */
public class ArrayFactory {

    public static final Array<?> EMPTY_ARRAY = newReadOnlyArray(ArrayUtils.EMPTY_OBJECT_ARRAY);

    /**
     * Create the new array.
     *
     * @param <E>  the type parameter
     * @param args the elements for the new array.
     * @return the new array.
     */
    @SafeVarargs
    public static <E> Array<E> asArray(@NotNull E... args) {
        Class<?> type = args.getClass().getComponentType();
        Array<E> array = new FastArray<>(unsafeCast(type), args.length);
        array.addAll(args);
        return array;
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

    /**
     * Create a new integer array.
     *
     * @return the new array.
     */
    public static @NotNull IntegerArray newIntegerArray() {
        return new FastIntegerArray();
    }

    /**
     * Create a new integer array.
     *
     * @param capacity the init size of the array.
     * @return the new array.
     */
    public static @NotNull IntegerArray newIntegerArray(int capacity) {
        return new FastIntegerArray(capacity);
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
     * Create the a float array.
     *
     * @param elements the elements of the new array.
     * @return the new array.
     */
    public static float[] toFloatArray(float... elements) {
        return elements;
    }

    /**
     * Create a new int array.
     *
     * @param elements the elements of the new array.
     * @return the new array.
     * @see #toIntArray(int...)
     */
    @Deprecated(forRemoval = true)
    public static int[] toIntegerArray(int... elements) {
        return elements;
    }

    /**
     * Create a new int array.
     *
     * @param elements the elements of the new array.
     * @return the new array.
     * @since 8.1.0
     */
    public static int[] toIntArray(int... elements) {
        return elements;
    }

    /**
     * Create a new object array.
     *
     * @param <T>      the type parameter
     * @param <K>      the type parameter
     * @param elements the elements of the new array.
     * @return the new array.
     */
    @SafeVarargs
    public static <T, K extends T> T[] toArray(K... elements) {
        return elements;
    }
}
