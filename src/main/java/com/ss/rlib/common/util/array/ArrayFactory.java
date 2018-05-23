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
     * Creates the new array.
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
     * Creates the new array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> Array<E> newArray(@NotNull Class<?> type) {
        return newUnsafeArray(type);
    }

    /**
     * Creates the new unsafe array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new unsafe array.
     */
    public static <E> UnsafeArray<E> newUnsafeArray(@NotNull Class<?> type) {
        Class<E> casted = unsafeCast(type);
        return new FastArray<>(casted);
    }

    /**
     * Creates the new array.
     *
     * @param <E>      the type parameter
     * @param type     the type of the array.
     * @param capacity the init size of the array.
     * @return the new array.
     */
    public static <E> Array<E> newArray(@NotNull Class<?> type, int capacity) {
        return newUnsafeArray(type, capacity);
    }

    /**
     * Creates a new read only array.
     *
     * @param <E> the element's type.
     * @return the new read only array.
     */
    public static <E> Array<E> newReadOnlyArray(@NotNull E[] elements) {
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
    public static <E> UnsafeArray<E> newUnsafeArray(@NotNull Class<?> type, int capacity) {
        return new FastArray<>(unsafeCast(type), capacity);
    }

    /**
     * Creates the new array set.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> Array<E> newArraySet(@NotNull Class<?> type) {
        return new FastArraySet<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentReentrantRWLockArray(@NotNull Class<?> type) {
        return new ConcurrentReentrantRWLockArray<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array set.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentReentrantRWLockArraySet(@NotNull Class<?> type) {
        return new ConcurrentReentrantRWLockArraySet<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentAtomicARSWLockArray(@NotNull Class<?> type) {
        return new ConcurrentAtomicARSWLockArray<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentStampedLockArray(@NotNull Class<?> type) {
        return new ConcurrentStampedLockArray<>(unsafeCast(type));
    }

    /**
     * Creates the new sorted array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E extends Comparable<E>> Array<E> newSortedArray(@NotNull Class<?> type) {
        return new SortedArray<>(unsafeCast(type));
    }

    /**
     * Creates the new synchronized array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> Array<E> newSynchronizedArray(@NotNull Class<?> type) {
        return new SynchronizedArray<>(unsafeCast(type));
    }

    /**
     * Creates the new integer array.
     *
     * @return the new array.
     */
    public static IntegerArray newIntegerArray() {
        return new FastIntegerArray();
    }

    /**
     * Creates the new long array.
     *
     * @return the new array.
     */
    public static LongArray newLongArray() {
        return new FastLongArray();
    }

    /**
     * Creates the new float array.
     *
     * @param elements the elements of the new array.
     * @return the new array.
     */
    public static float[] toFloatArray(float... elements) {
        return elements;
    }

    /**
     * Creates the new int array.
     *
     * @param elements the elements of the new array.
     * @return the new array.
     */
    public static int[] toIntegerArray(int... elements) {
        return elements;
    }

    /**
     * Creates the new object array.
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
