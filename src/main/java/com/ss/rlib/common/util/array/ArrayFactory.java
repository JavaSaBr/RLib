package com.ss.rlib.common.util.array;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;

import com.ss.rlib.common.util.array.impl.*;
import com.ss.rlib.common.util.array.impl.ConcurrentReentrantRWLockArraySet;
import com.ss.rlib.common.util.array.impl.ConcurrentReentrantRWLockArray;
import com.ss.rlib.common.util.array.impl.FastIntegerArray;
import com.ss.rlib.common.util.array.impl.FastLongArray;
import com.ss.rlib.common.util.array.impl.FinalConcurrentAtomicARSWLockArray;
import com.ss.rlib.common.util.array.impl.FinalConcurrentStampedLockArray;
import com.ss.rlib.common.util.array.impl.FinalFastArray;
import com.ss.rlib.common.util.array.impl.FinalFastArraySet;
import com.ss.rlib.common.util.array.impl.FinalSortedArray;
import com.ss.rlib.common.util.array.impl.FinalSynchronizedArray;

/**
 * The factory for creating arrays.
 *
 * @author JavaSaBr
 */
public class ArrayFactory {

    /**
     * Creates the new array.
     *
     * @param <E>  the type parameter
     * @param args the elements for the new array.
     * @return the new array.
     */
    @SafeVarargs
    public static <E> Array<E> asArray(final E... args) {
        final Class<?> type = args.getClass().getComponentType();
        final FinalFastArray<E> array = new FinalFastArray<>(unsafeCast(type), args.length);
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
    public static <E> Array<E> newArray(final Class<?> type) {
        return newUnsafeArray(type);
    }

    /**
     * Creates the new unsafe array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new unsafe array.
     */
    public static <E> UnsafeArray<E> newUnsafeArray(final Class<?> type) {
        return new FinalFastArray<>(unsafeCast(type));
    }

    /**
     * Creates the new array.
     *
     * @param <E>      the type parameter
     * @param type     the type of the array.
     * @param capacity the init size of the array.
     * @return the new array.
     */
    public static <E> Array<E> newArray(final Class<?> type, final int capacity) {
        return newUnsafeArray(type, capacity);
    }

    /**
     * Creates the new unsafe array.
     *
     * @param <E>      the type parameter
     * @param type     the type of the array.
     * @param capacity the init size of the array.
     * @return the new unsafe array.
     */
    public static <E> UnsafeArray<E> newUnsafeArray(final Class<?> type, final int capacity) {
        return new FinalFastArray<>(unsafeCast(type), capacity);
    }

    /**
     * Creates the new array set.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> Array<E> newArraySet(final Class<?> type) {
        return new FinalFastArraySet<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentReentrantRWLockArray(final Class<?> type) {
        return new ConcurrentReentrantRWLockArray<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array set.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentReentrantRWLockArraySet(final Class<?> type) {
        return new ConcurrentReentrantRWLockArraySet<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentAtomicARSWLockArray(final Class<?> type) {
        return new FinalConcurrentAtomicARSWLockArray<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> ConcurrentArray<E> newConcurrentStampedLockArray(final Class<?> type) {
        return new FinalConcurrentStampedLockArray<>(unsafeCast(type));
    }

    /**
     * Creates the new sorted array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E extends Comparable<E>> Array<E> newSortedArray(final Class<?> type) {
        return new FinalSortedArray<>(unsafeCast(type));
    }

    /**
     * Creates the new synchronized array.
     *
     * @param <E>  the type parameter
     * @param type the type of the array.
     * @return the new array.
     */
    public static <E> Array<E> newSynchronizedArray(final Class<?> type) {
        return new FinalSynchronizedArray<>(unsafeCast(type));
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
    public static float[] toFloatArray(final float... elements) {
        return elements;
    }

    /**
     * Creates the new int array.
     *
     * @param elements the elements of the new array.
     * @return the new array.
     */
    public static int[] toIntegerArray(final int... elements) {
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
    public static <T, K extends T> T[] toArray(final K... elements) {
        return elements;
    }
}
