package com.ss.rlib.common.util.array;

import static com.ss.rlib.common.util.ClassUtils.unsafeNNCast;
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

    @SafeVarargs
    public static <E> @NotNull Array<E> asArray(@NotNull E... args) {
        return new FastArray<>(args);
    }

    public static <E> @NotNull Array<E> newArray(@NotNull Class<? super E> type) {
        return newUnsafeArray(type);
    }

    public static <E> @NotNull Array<E> newArray(@NotNull Class<? super E> type, int capacity) {
        return newUnsafeArray(type, capacity);
    }

    public static <E> @NotNull Array<E> newCopyOnModifyArray(@NotNull Class<? super E> type) {
        return new CopyOnModifyArray<>(type, 0);
    }

    public static <E> @NotNull UnsafeArray<E> newUnsafeArray(@NotNull Class<? super E> type) {
        return new FastArray<>(type);
    }

    public static <E> UnsafeArray<E> newUnsafeArray(@NotNull Class<? super E> type, int capacity) {
        return new FastArray<>(type, capacity);
    }

    public static <E> @NotNull ReadOnlyArray<E> newReadOnlyArray(@NotNull E[] elements) {
        return new ReadOnlyFastArray<>(elements);
    }

    public static <E> @NotNull Array<E> newArraySet(@NotNull Class<? super E> type) {
        return new FastArraySet<>(type);
    }

    public static <E> @NotNull ConcurrentArray<E> newConcurrentReentrantRWLockArray(@NotNull Class<? super E> type) {
        return new ConcurrentReentrantRWLockArray<>(type);
    }

    public static <E> @NotNull ConcurrentArray<E> newConcurrentReentrantRWLockArraySet(@NotNull Class<? super E> type) {
        return new ConcurrentReentrantRWLockArraySet<>(type);
    }

    public static <E> @NotNull ConcurrentArray<E> newConcurrentAtomicARSWLockArray(@NotNull Class<? super E> type) {
        return new ConcurrentAtomicARSWLockArray<>(type);
    }

    public static <E> @NotNull ConcurrentArray<E> newConcurrentStampedLockArray(@NotNull Class<? super E> type) {
        return new ConcurrentStampedLockArray<>(type);
    }

    public static <E> @NotNull ConcurrentArray<E> newConcurrentStampedLockArray(E @NotNull [] array) {

        Class<? super E> type = unsafeNNCast(array.getClass().getComponentType());

        var result = new ConcurrentStampedLockArray<E>(type, array.length);
        result.addAll(array);

        return result;
    }

    public static <E> @NotNull ConcurrentArray<E> newConcurrentStampedLockArraySet(@NotNull Class<? super E> type) {
        return new ConcurrentStampedLockArraySet<>(type);
    }

    public static <E extends Comparable<E>> @NotNull Array<E> newSortedArray(@NotNull Class<? super E> type) {
        return new SortedFastArray<>(unsafeNNCast(type));
    }

    public static <E> @NotNull Array<E> newSynchronizedArray(@NotNull Class<? super E> type) {
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

    public static @NotNull LongArray newLongArray() {
        return new FastLongArray();
    }

    public static @NotNull LongArray newLongArray(int capacity) {
        return new FastLongArray(capacity);
    }

    public static @NotNull float[] toFloatArray(float... elements) {
        return elements;
    }

    public static @NotNull int[] toIntArray(int... elements) {
        return elements;
    }

    public static @NotNull long[] toLongArray(long... elements) {
        return elements;
    }

    public static @NotNull boolean[] toBooleanArray(boolean... elements) {
        return elements;
    }

    @SafeVarargs
    public static <T, K extends T> @NotNull T[] toArray(K... elements) {
        return elements;
    }
}
