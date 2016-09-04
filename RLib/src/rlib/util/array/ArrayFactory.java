package rlib.util.array;

import rlib.util.array.impl.ConcurrentReentrantRWLockArray;
import rlib.util.array.impl.ConcurrentReentrantRWLockArraySet;
import rlib.util.array.impl.FastIntegerArray;
import rlib.util.array.impl.FastLongArray;
import rlib.util.array.impl.FinalConcurrentAtomicARSWLockArray;
import rlib.util.array.impl.FinalConcurrentStampedLockArray;
import rlib.util.array.impl.FinalFastArray;
import rlib.util.array.impl.FinalFastArraySet;
import rlib.util.array.impl.FinalSortedArray;
import rlib.util.array.impl.FinalSynchronizedArray;

import static rlib.util.ClassUtils.unsafeCast;

/**
 * The factory for creating arrays.
 *
 * @author JavaSaBr
 */
public class ArrayFactory {

    /**
     * Creates the new array.
     *
     * @param args the elements for the new array.
     * @return the new array.
     * @see {@link FinalFastArray}
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
     * @param type the type of the array.
     * @return the new array.
     * @see {@link FinalFastArray}
     */
    public static <E> Array<E> newArray(final Class<?> type) {
        return new FinalFastArray<>(unsafeCast(type));
    }

    /**
     * Creates the new array.
     *
     * @param type     the type of the array.
     * @param capacity the init size of the array.
     * @return the new array.
     * @see {@link FinalFastArray}
     */
    public static <E> Array<E> newArray(final Class<?> type, final int capacity) {
        return new FinalFastArray<>(unsafeCast(type), capacity);
    }

    /**
     * Creates the new array set.
     *
     * @param type the type of the array.
     * @return the new array.
     * @see {@link FinalFastArraySet}
     */
    public static <E> Array<E> newArraySet(final Class<?> type) {
        return new FinalFastArraySet<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array.
     *
     * @param type the type of the array.
     * @return the new array.
     * @see {@link ConcurrentReentrantRWLockArray}
     */
    public static <E> ConcurrentArray<E> newConcurrentReentrantRWLockArray(final Class<?> type) {
        return new ConcurrentReentrantRWLockArray<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array set.
     *
     * @param type the type of the array.
     * @return the new array.
     * @see {@link ConcurrentReentrantRWLockArraySet}
     */
    public static <E> ConcurrentArray<E> newConcurrentReentrantRWLockArraySet(final Class<?> type) {
        return new ConcurrentReentrantRWLockArraySet<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array.
     *
     * @param type the type of the array.
     * @return the new array.
     * @see {@link FinalConcurrentAtomicARSWLockArray}
     */
    public static <E> ConcurrentArray<E> newConcurrentAtomicARSWLockArray(final Class<?> type) {
        return new FinalConcurrentAtomicARSWLockArray<>(unsafeCast(type));
    }

    /**
     * Creates the new concurrent array.
     *
     * @param type the type of the array.
     * @return the new array.
     * @see {@link FinalConcurrentAtomicARSWLockArray}
     */
    public static <E> ConcurrentArray<E> newConcurrentStampedLockArray(final Class<?> type) {
        return new FinalConcurrentStampedLockArray<>(unsafeCast(type));
    }

    /**
     * Creates the new sorted array.
     *
     * @param type the type of the array.
     * @return the new array.
     * @see {@link FinalSortedArray}
     */
    public static <E extends Comparable<E>> Array<E> newSortedArray(final Class<?> type) {
        return new FinalSortedArray<>(unsafeCast(type));
    }

    /**
     * Creates the new synchronized array.
     *
     * @param type the type of the array.
     * @return the new array.
     * @see {@link FinalSynchronizedArray}
     */
    public static <E> Array<E> newSynchronizedArray(final Class<?> type) {
        return new FinalSynchronizedArray<>(unsafeCast(type));
    }

    /**
     * Creates the new integer array.
     *
     * @return the new array.
     * @see {@link FastIntegerArray}
     */
    public static IntegerArray newIntegerArray() {
        return new FastIntegerArray();
    }

    /**
     * Creates the new long array.
     *
     * @return the new array.
     * @see {@link FastLongArray}
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
     * @param elements the elements of the new array.
     * @return the new array.
     */
    @SafeVarargs
    public static <T, K extends T> T[] toArray(final K... elements) {
        return elements;
    }
}
