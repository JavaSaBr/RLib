package com.ss.rlib.common.util;

import com.ss.rlib.common.function.*;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ConcurrentArray;
import com.ss.rlib.common.util.array.IntegerArray;
import com.ss.rlib.common.util.array.LongArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

/**
 * The utility class.
 *
 * @author JavaSaBr
 */
public final class ArrayUtils {

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final Enum<?>[] EMPTY_ENUM_ARRAY = new Enum[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];

    /**
     * Add the element to the array and extend or create the array if need.
     *
     * @param <T>     the type parameter
     * @param array   the array.
     * @param element the element.
     * @param type    the type of array.
     * @return the result array with added element.
     */
    public static <T> @NotNull T[] addToArray(@Nullable T[] array, @NotNull T element, @NotNull Class<T> type) {

        if (array == null) {
            array = create(type, 1);
            array[0] = element;
            return array;
        }

        int length = array.length;

        array = copyOf(array, 1);
        array[length] = element;

        return array;
    }

    /**
     * Clear all elements in the array.
     *
     * @param array the array.
     */
    public static void clear(@NotNull Object[] array) {
        for (int i = 0, length = array.length; i < length; i++) {
            array[i] = null;
        }
    }

    /**
     * Fill the array using the factory.
     *
     * @param array   the array.
     * @param factory the element's factory.
     * @param <T>     the element's type.
     */
    public static <T> void fill(@NotNull T[] array, @NotNull Supplier<T> factory) {
        for (int i = 0; i < array.length; i++) {
            array[i] = factory.get();
        }
    }

    /**
     * Fill the array using the factory.
     *
     * @param array   the array.
     * @param factory the element's factory.
     * @since 8.1.0
     */
    public static void fill(@NotNull char[] array, @NotNull CharSupplier factory) {
        for (int i = 0; i < array.length; i++) {
            array[i] = factory.getAsChar();
        }
    }

    /**
     * Fill the array using the factory which receives array's index.
     *
     * @param array   the array.
     * @param factory the element's factory.
     * @param <T>     the element's type.
     */
    public static <T> void fill(@NotNull T[] array, @NotNull IntFunction<T> factory) {
        for (int i = 0; i < array.length; i++) {
            array[i] = factory.apply(i);
        }
    }

    /**
     * Fill the array using the factory.
     *
     * @param array    the array.
     * @param argument the additional argument.
     * @param factory  the element's factory.
     * @param <T>      the element's type.
     */
    public static <T, F> void fill(@NotNull T[] array, @Nullable F argument, @NotNull Function<F, T> factory) {
        for (int i = 0; i < array.length; i++) {
            array[i] = factory.apply(argument);
        }
    }

    /**
     * Combine the two arrays.
     *
     * @param base  the source array.
     * @param added the add array.
     * @return the combined array.
     */
    public static @NotNull int[] combine(@Nullable final int[] base, @Nullable final int[] added) {

        if (base == null) {
            return added == null ? new int[0] : added;
        } else if (added == null || added.length < 1) {
            return base;
        }

        final int[] result = new int[base.length + added.length];

        int index = 0;

        for (int val : base) result[index++] = val;
        for (int val : added) result[index++] = val;

        return result;
    }

    /**
     * Combine the two arrays.
     *
     * @param <T>   the base array's component type.
     * @param <E>   the added array's component type.
     * @param base  the source array.
     * @param added the add array.
     * @return the combined array.
     */
    public static <T, E extends T> @NotNull T[] combine(@Nullable T[] base, @Nullable E[] added) {
        return combine(base, added, ClassUtils.unsafeCast(base.getClass().getComponentType()));
    }

    /**
     * Combine the two arrays.
     *
     * @param <T>   the base array's component type.
     * @param <E>   the added array's component type.
     * @param base  the source array.
     * @param added the add array.
     * @param type  the base array's component type.
     * @return the combined array.
     */
    public static <T, E extends T> @NotNull T[] combine(
            @Nullable T[] base,
            @Nullable E[] added,
            @NotNull Class<T> type
    ) {

        if (base == null) {
            return added == null ? create(type, 0) : added;
        } else if (added == null || added.length < 1) {
            return base;
        }

        T[] result = create(type, base.length + added.length);

        int index = 0;

        for (T object : base) {
            result[index++] = object;
        }

        for (E object : added) {
            result[index++] = object;
        }

        return result;
    }

    /**
     * Check the array on contains the value.
     *
     * @param array the array.
     * @param val   the value.
     * @return true if the array contains the value.
     */
    public static boolean contains(@NotNull final int[] array, final int val) {
        for (final int value : array) {
            if (value == val) return true;
        }
        return false;
    }

    /**
     * Check the array on contains the object.
     *
     * @param array  the array.
     * @param object the object.
     * @return true if the array contains the object.
     */
    public static boolean contains(@NotNull final Object[] array, @Nullable final Object object) {
        for (final Object element : array) {
            if (Objects.equals(element, object)) return true;
        }
        return false;
    }

    /**
     * Copy and extend the array.
     *
     * @param old   the source array.
     * @param added the added size.
     * @return the new array.
     */
    public static @NotNull byte[] copyOf(@NotNull final byte[] old, final int added) {
        final byte[] copy = new byte[old.length + added];
        System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));
        return copy;
    }

    /**
     * Copy and extend the array.
     *
     * @param old   the source array.
     * @param added the added size.
     * @return the new array.
     */
    public static @NotNull int[] copyOf(@NotNull final int[] old, final int added) {
        final int[] copy = new int[old.length + added];
        System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));
        return copy;
    }

    /**
     * Copy and extend the array.
     *
     * @param old   the source array.
     * @param added the added size.
     * @return the new array.
     */
    public static @NotNull long[] copyOf(@NotNull final long[] old, final int added) {
        final long[] copy = new long[old.length + added];
        System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));
        return copy;
    }

    /**
     * Copy and extend the array.
     *
     * @param <T>      the array component's type.
     * @param original the source array.
     * @param added    the added size.
     * @return the new array.
     */
    public static <T> @NotNull T[] copyOf(@NotNull T[] original, int added) {

        Class<? extends Object[]> newType = original.getClass();
        Object[] newArray = create(newType.getComponentType(), original.length + added);

        T[] copy = ClassUtils.unsafeCast(newArray);

        System.arraycopy(original, 0, copy, 0, Math.min(original.length, copy.length));

        return copy;
    }

    /**
     * Copy and extend the array.
     *
     * @param <T>      the array component's type.
     * @param original the source array.
     * @param offset   the start position to copy in new array.
     * @param added    the added size.
     * @return the new array.
     */
    public static <T> @NotNull T[] copyOf(@NotNull T[] original, int offset, int added) {

        Class<? extends Object[]> newType = original.getClass();
        T[] newArray = create(newType.getComponentType(), original.length + added);

        System.arraycopy(original, 0, newArray, offset, Math.min(original.length, newArray.length));

        return newArray;
    }

    /**
     * Copy data form the source array to the destination array.
     *
     * @param source the source array.
     * @param target the target array.
     */
    public static void copyTo(@NotNull final int[] source, final int[] target) {
        System.arraycopy(source, 0, target, 0, source.length);
    }

    /**
     * Copy data form the source array to the destination array.
     *
     * @param source       the source array.
     * @param target       the target array.
     * @param sourceOffset the source offset.
     * @param targetOffset the target offset.
     * @param length       the length of data.
     */
    public static void copyTo(@NotNull final int[] source, final int[] target, final int sourceOffset,
                              final int targetOffset, final int length) {
        System.arraycopy(source, sourceOffset, target, targetOffset, length);
    }

    /**
     * Copy a part of the array to a new array.
     *
     * @param original the source array.
     * @param from     the start element.
     * @param to       the last element.
     * @return the new array.
     */
    public static @NotNull int[] copyOfRange(@NotNull final int[] original, final int from, final int to) {

        final int newLength = to - from;
        final int[] copy = new int[newLength];

        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));

        return copy;
    }

    /**
     * Copy a part of the array to a new array.
     *
     * @param original the source array.
     * @param from     the start element.
     * @param to       the last element.
     * @return the new array.
     */
    public static @NotNull long[] copyOfRange(@NotNull final long[] original, final int from, final int to) {

        final int newLength = to - from;
        final long[] copy = new long[newLength];

        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));

        return copy;
    }

    /**
     * Copy a part of the array to a new array.
     *
     * @param <T>      the type parameter
     * @param original the source array.
     * @param from     the start element.
     * @param to       the last element.
     * @return the new array.
     */
    public static <T> @NotNull T[] copyOfRange(@NotNull final T[] original, final int from, final int to) {

        final Class<? extends Object[]> newType = original.getClass();
        final int newLength = to - from;

        final T[] copy = ClassUtils.unsafeCast(create(newType.getComponentType(), newLength));

        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));

        return copy;
    }

    /**
     * Create the array by the type.
     *
     * @param <T>  the type parameter
     * @param type the array type.
     * @param size the size.
     * @return the new array.
     */
    public static <T> @NotNull T[] create(@NotNull Class<?> type, int size) {
        return ClassUtils.unsafeCast(java.lang.reflect.Array.newInstance(type, size));
    }

    /**
     * Find an index of the object in the array.
     *
     * @param array  the array.
     * @param object the object.
     * @return the object's index or -1.
     */
    public static int indexOf(@NotNull final Object[] array, @Nullable final Object object) {

        int index = 0;

        for (final Object element : array) {
            if (Objects.equals(element, object)) return index;
            index++;
        }

        return -1;
    }

    /**
     * Sort the array.
     *
     * @param array the array.
     */
    public static void sort(@NotNull final Comparable<?>[] array) {
        java.util.Arrays.sort(array);
    }

    /**
     * Sort the array.
     *
     * @param array the array.
     */
    public static void sort(@NotNull final int[] array) {
        java.util.Arrays.sort(array);
    }

    /**
     * Sort the array.
     *
     * @param array     the array.
     * @param fromIndex the start index.
     * @param toIndex   the last index.
     */
    public static void sort(@NotNull final int[] array, final int fromIndex, final int toIndex) {
        java.util.Arrays.sort(array, fromIndex, toIndex);
    }

    /**
     * Sort the array.
     *
     * @param array     the array.
     * @param fromIndex the start index.
     * @param toIndex   the last index.
     */
    public static void sort(@NotNull final long[] array, final int fromIndex, final int toIndex) {
        java.util.Arrays.sort(array, fromIndex, toIndex);
    }

    /**
     * Sort the array.
     *
     * @param <T>        the type parameter
     * @param array      the array.
     * @param comparator the comparator.
     */
    public static <T> void sort(@NotNull final T[] array, @NotNull final Comparator<? super T> comparator) {
        java.util.Arrays.sort(array, comparator);
    }

    /**
     * Sort the array.
     *
     * @param <T>        the type parameter
     * @param array      the array.
     * @param fromIndex  the start index.
     * @param toIndex    the last index.
     * @param comparator the comparator.
     */
    public static <T> void sort(@NotNull final T[] array, final int fromIndex, final int toIndex,
                                @NotNull final Comparator<? super T> comparator) {
        java.util.Arrays.sort(array, fromIndex, toIndex, comparator);
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array the array.
     * @return the string presentation.
     */
    public static @NotNull String toString(@NotNull Array<?> array) {
        return toString(array, Object::toString);
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array    the array.
     * @param toString the to string function.
     * @return the string presentation.
     */
    public static <T> @NotNull String toString(@NotNull Array<T> array, @NotNull Function<T, String> toString) {

        if (array.isEmpty()) {
            return "[]";
        }

        String className = array.array()
                .getClass()
                .getSimpleName();

        StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.size() - 1; i <= length; i++) {

            builder.append(toString.apply(array.get(i)));

            if (i == length) {
                break;
            }

            builder.append(", ");
        }

        builder.append("]");

        return builder.toString();
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array the array.
     * @return the string presentation.
     */
    public static @NotNull String toString(@NotNull final IntegerArray array) {

        final String className = array.array().getClass().getSimpleName();
        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.size() - 1; i <= length; i++) {
            builder.append(String.valueOf(array.get(i)));
            if (i == length) break;
            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array the array.
     * @return the string presentation.
     */
    public static @NotNull String toString(@NotNull final LongArray array) {

        final String className = array.array().getClass().getSimpleName();
        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.size() - 1; i <= length; i++) {
            builder.append(String.valueOf(array.get(i)));
            if (i == length) break;
            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array the array.
     * @return the string presentation.
     */
    public static @NotNull String toString(@Nullable int[] array) {
        return toString(array, ", ", true, true);
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array        the array.
     * @param separator    the separator.
     * @param needType     true if need adding type of array.
     * @param needBrackets true if need adding brackets.
     * @return the string presentation of the array.
     */
    public static @NotNull String toString(@Nullable int[] array, @NotNull final String separator,
                                           final boolean needType, final boolean needBrackets) {

        if (array == null) {
            array = EMPTY_INT_ARRAY;
        }

        final StringBuilder builder = new StringBuilder();

        if (needType) builder.append("int");
        if (needBrackets) builder.append('[');

        for (int i = 0, length = array.length - 1; i <= length; i++) {
            builder.append(String.valueOf(array[i]));
            if (i == length) break;
            builder.append(separator);
        }

        if (needBrackets) builder.append(']');

        return builder.toString();
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array the array.
     * @return the string presentation.
     */
    public static @NotNull String toString(@Nullable float[] array) {
        return toString(array, ", ", true, true);
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array        the array.
     * @param separator    the separator.
     * @param needType     true if need adding type of array.
     * @param needBrackets true if need adding brackets.
     * @return the string presentation of the array.
     */
    public static @NotNull String toString(@Nullable float[] array, @NotNull final String separator,
                                           final boolean needType, final boolean needBrackets) {

        if (array == null) {
            array = EMPTY_FLOAT_ARRAY;
        }

        final StringBuilder builder = new StringBuilder();

        if (needType) builder.append("float");
        if (needBrackets) builder.append('[');

        for (int i = 0, length = array.length - 1; i <= length; i++) {
            builder.append(String.valueOf(array[i]));
            if (i == length) break;
            builder.append(separator);
        }

        if (needBrackets) builder.append(']');

        return builder.toString();
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array the array.
     * @return the string presentation of the array.
     */
    public static @NotNull String toString(@Nullable final Object[] array) {
        return toString(array, ", ", true, true);
    }

    /**
     * Convert the array to a string presentation.
     *
     * @param array        the array.
     * @param separator    the separator.
     * @param needType     true if need adding type of array.
     * @param needBrackets true if need adding brackets.
     * @return the string presentation of the array.
     */
    public static @NotNull String toString(@Nullable Object[] array, @NotNull final String separator,
                                           final boolean needType, final boolean needBrackets) {

        if (array == null) {
            array = EMPTY_OBJECT_ARRAY;
        }

        final StringBuilder builder = new StringBuilder();

        if (needType) builder.append(array.getClass().getSimpleName());
        if (needBrackets) builder.append('[');

        for (int i = 0, length = array.length - 1; i <= length; i++) {
            builder.append(String.valueOf(array[i]));
            if (i == length) break;
            builder.append(separator);
        }

        if (needBrackets) builder.append(']');

        return builder.toString();
    }

    /**
     * Execute and get a result of the function in write lock of the array.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param array    the array.
     * @param function the function.
     * @return the result of the function.
     */
    @Deprecated
    public static <T, R> @Nullable R getInWriteLock(@NotNull final ConcurrentArray<T> array,
                                                    @NotNull final Function<Array<T>, R> function) {
        if (array.isEmpty()) return null;
        final long stamp = array.writeLock();
        try {
            return function.apply(array);
        } finally {
            array.writeUnlock(stamp);
        }
    }

    /**
     * Execute the function in write lock of the array.
     *
     * @param <T>      the element's type.
     * @param array    the array.
     * @param function the function.
     */
    @Deprecated
    public static <T> void runInWriteLock(@Nullable ConcurrentArray<T> array, @NotNull Consumer<Array<T>> function) {

        if (array == null) {
            return;
        }

        long stamp = array.writeLock();
        try {
            function.accept(array);
        } finally {
            array.writeUnlock(stamp);
        }
    }

    /**
     * Execute the function in read lock of the array.
     *
     * @param <T>      the type parameter
     * @param array    the array.
     * @param function the function.
     */
    @Deprecated
    public static <T> void runInReadLock(@NotNull final ConcurrentArray<T> array,
                                         @NotNull final Consumer<@NotNull Array<T>> function) {
        if (array.isEmpty()) return;
        final long stamp = array.readLock();
        try {
            function.accept(array);
        } finally {
            array.readUnlock(stamp);
        }
    }

    /**
     * Execute and get a result of the function in read lock of the array.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param array    the array.
     * @param function the function.
     * @return the result of the function.
     */
    @Deprecated
    public static <T, R> @Nullable R getInReadLock(@NotNull final ConcurrentArray<T> array,
                                                   @NotNull final Function<Array<T>, R> function) {
        if (array.isEmpty()) return null;
        final long stamp = array.readLock();
        try {
            return function.apply(array);
        } finally {
            array.readUnlock(stamp);
        }
    }

    /**
     * Execute and sum a result of the function in read lock of the array.
     *
     * @param <T>      the type parameter
     * @param array    the array.
     * @param function the function.
     * @return the sum.
     */
    @Deprecated
    public static <T> int sumInReadLock(@NotNull final ConcurrentArray<T> array,
                                        @NotNull final FunctionInt<T> function) {
        if (array.isEmpty()) return 0;
        final long stamp = array.readLock();
        try {

            int sum = 0;

            for (T element : array) {
                if (element == null) break;
                sum += function.apply(element);
            }

            return sum;

        } finally {
            array.readUnlock(stamp);
        }
    }

    /**
     * Execute and get a result of the function in write lock of the array.
     *
     * @param <T>      the type parameter
     * @param <V>      the type parameter
     * @param <R>      the type parameter
     * @param array    the array.
     * @param argument the argument.
     * @param function the function.
     * @return the result of the function.
     */
    @Deprecated
    public static <T, V, R> @Nullable R getInWriteLock(@NotNull final ConcurrentArray<T> array,
                                                       @Nullable final V argument,
                                                       @NotNull final BiFunction<Array<T>, V, R> function) {
        if (array.isEmpty()) return null;
        final long stamp = array.writeLock();
        try {
            return function.apply(array, argument);
        } finally {
            array.writeUnlock(stamp);
        }
    }

    /**
     * Execute the function in write lock of the array.
     *
     * @param <T>      the element's type.
     * @param <V>      the argument's type.
     * @param array    the array.
     * @param argument the argument.
     * @param function the function.
     */
    @Deprecated
    public static <T, V> void runInWriteLock(
            @NotNull ConcurrentArray<T> array,
            @Nullable V argument,
            @NotNull BiConsumer<@NotNull Array<T>, V> function
    ) {
        long stamp = array.writeLock();
        try {
            function.accept(array, argument);
        } finally {
            array.writeUnlock(stamp);
        }
    }

    /**
     * Execute and get a result of the function in read lock of the array.
     *
     * @param <T>      the type parameter
     * @param <V>      the type parameter
     * @param <R>      the type parameter
     * @param array    the array.
     * @param argument the argument.
     * @param function the function.
     * @return the result of the function.
     */
    @Deprecated
    public static <T, V, R> @Nullable R getInReadLock(@NotNull final ConcurrentArray<T> array,
                                                      @Nullable final V argument,
                                                      @NotNull final BiFunction<Array<T>, V, R> function) {
        if (array.isEmpty()) return null;
        final long stamp = array.readLock();
        try {
            return function.apply(array, argument);
        } finally {
            array.readUnlock(stamp);
        }
    }

    /**
     * Execute and get a result of the function in read lock of the array.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param array    the array.
     * @param argument the argument.
     * @param function the function.
     * @return the result of the function.
     */
    @Deprecated
    public static <T, R> @Nullable R getInReadLock(@NotNull final ConcurrentArray<T> array, final int argument,
                                                   @NotNull final ObjectIntFunction<Array<T>, R> function) {
        if (array.isEmpty()) return null;
        final long stamp = array.readLock();
        try {
            return function.apply(array, argument);
        } finally {
            array.readUnlock(stamp);
        }
    }

    /**
     * Execute and get a result of the function in read lock of the array.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param array    the array.
     * @param argument the argument.
     * @param function the function.
     * @return the result of the function.
     */
    @Deprecated
    public static <T, R> @Nullable R getInReadLockL(@NotNull final ConcurrentArray<T> array, final long argument,
                                                    @NotNull final ObjectLongFunction<Array<T>, R> function) {
        if (array.isEmpty()) return null;
        final long stamp = array.readLock();
        try {
            return function.apply(array, argument);
        } finally {
            array.readUnlock(stamp);
        }
    }

    /**
     * Execute the function in read lock of the array.
     *
     * @param <T>      the type parameter
     * @param <V>      the type parameter
     * @param array    the array.
     * @param argument the argument.
     * @param function the function.
     */
    @Deprecated
    public static <T, V> void runInReadLock(@NotNull final ConcurrentArray<T> array, @Nullable final V argument,
                                            @NotNull final BiConsumer<@NotNull Array<T>, V> function) {
        if (array.isEmpty()) return;
        final long stamp = array.readLock();
        try {
            function.accept(array, argument);
        } finally {
            array.readUnlock(stamp);
        }
    }

    /**
     * Execute the function in write lock of the array.
     *
     * @param <T>      the type parameter
     * @param <F>      the type parameter
     * @param <S>      the type parameter
     * @param array    the array.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     */
    @Deprecated
    public static <T, F, S> void runInWriteLock(@NotNull final ConcurrentArray<T> array, @Nullable final F first,
                                                @Nullable S second,
                                                @NotNull final TripleConsumer<@NotNull Array<T>, F, S> function) {
        final long stamp = array.writeLock();
        try {
            function.accept(array, first, second);
        } finally {
            array.writeUnlock(stamp);
        }
    }

    /**
     * Execute the function in write lock of the array.
     *
     * @param <T>      the type parameter
     * @param <F>      the type parameter
     * @param <S>      the type parameter
     * @param array    the array.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param filter   the filter.
     * @param function the function.
     */
    @Deprecated
    public static <T, F, S> void runInWriteLock(@NotNull final ConcurrentArray<T> array, @Nullable final F first,
                                                @Nullable final S second,
                                                @NotNull final TriplePredicate<@NotNull Array<T>, F, S> filter,
                                                @NotNull final TripleConsumer<@NotNull Array<T>, F, S> function) {
        final long stamp = array.writeLock();
        try {

            if (filter.test(array, first, second)) {
                function.accept(array, first, second);
            }

        } finally {
            array.writeUnlock(stamp);
        }
    }

    /**
     * Execute the function in read lock of the array.
     *
     * @param <T>      the type parameter
     * @param <F>      the type parameter
     * @param <S>      the type parameter
     * @param array    the array.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     */
    @Deprecated
    public static <T, F, S> void runInReadLock(@NotNull final ConcurrentArray<T> array, @Nullable final F first,
                                               @Nullable final S second,
                                               @NotNull final TripleConsumer<@NotNull Array<T>, F, S> function) {
        if (array.isEmpty()) return;
        final long stamp = array.readLock();
        try {
            function.accept(array, first, second);
        } finally {
            array.readUnlock(stamp);
        }
    }

    /**
     * Move all elements from the source array to the destination array.
     *
     * @param <F>         the type parameter
     * @param <S>         the type parameter
     * @param source      the source array.
     * @param destination the destination array.
     * @param clearSource need clears the source array after moving.
     */
    public static <F extends S, S> void move(@NotNull final Array<F> source, @NotNull final Array<S> destination,
                                             final boolean clearSource) {
        if (source.isEmpty()) return;
        destination.addAll(source);
        if (clearSource) source.clear();
    }

    /**
     * Move all elements from the source array to the destination array.
     *
     * @param <F>         the type parameter
     * @param <S>         the type parameter
     * @param source      the source array.
     * @param destination the destination array.
     */
    public static <F extends S, S> void move(@NotNull final Array<F> source, @NotNull final Array<S> destination) {
        move(source, destination, true);
    }

    /**
     * Add the object to the array in a synchronize block.
     *
     * @param <T>    the type parameter
     * @param <V>    the type parameter
     * @param array  the array.
     * @param object the object.
     */
    public static <T, V extends T> void addInSynchronizeTo(@NotNull final Array<T> array, @NotNull final V object) {
        synchronized (array) {
            array.add(object);
        }
    }

    /**
     * Fast remove the object from the array in a synchronize block.
     *
     * @param <T>    the type parameter
     * @param <V>    the type parameter
     * @param array  the array.
     * @param object the object.
     */
    public static <T, V extends T> void fastRemoveInSynchronizeTo(@NotNull final Array<T> array,
                                                                  @NotNull final V object) {
        synchronized (array) {
            array.fastRemove(object);
        }
    }

    /**
     * Apply the function to each element of the array.
     *
     * @param <T>      the type parameter
     * @param array    the array.
     * @param function the function.
     */
    public static <T> void forEach(@Nullable final T[] array, @NotNull final Consumer<T> function) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            function.accept(element);
        }
    }

    /**
     * Apply the function to each filtered element of the array.
     *
     * @param <T>       the type parameter
     * @param array     the array.
     * @param condition the condition.
     * @param function  the function.
     */
    public static <T> void forEach(@Nullable final T[] array, @NotNull final Predicate<T> condition,
                                   @NotNull final Consumer<T> function) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            if (condition.test(element)) {
                function.accept(element);
            }
        }
    }

    /**
     * Apply the consumer for each element of the array.
     *
     * @param <F>      the type parameter
     * @param array    the array.
     * @param argument the additional argument.
     * @param consumer the consumer.
     */
    public static <F> void forEach(@Nullable final int[] array, @Nullable final F argument,
                                   @NotNull final IntObjectConsumer<F> consumer) {
        if (array == null || array.length < 1) return;
        for (final int element : array) {
            consumer.accept(element, argument);
        }
    }

    /**
     * Apply the consumer for each element of the array.
     *
     * @param <F>      the type parameter
     * @param array    the array.
     * @param argument the additional argument.
     * @param consumer the consumer.
     */
    public static <F> void forEach(@Nullable final double[] array, @Nullable final F argument,
                                   @NotNull final DoubleObjectConsumer<F> consumer) {
        if (array == null || array.length < 1) return;
        for (final double element : array) {
            consumer.accept(element, argument);
        }
    }

    /**
     * Apply the consumer for each element of the array.
     *
     * @param <T>      the type parameter
     * @param <F>      the type parameter
     * @param array    the array.
     * @param argument the additional argument.
     * @param consumer the consumer.
     */
    public static <T, F> void forEach(@Nullable final T[] array, @Nullable final F argument,
                                      @NotNull final BiConsumer<T, F> consumer) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            consumer.accept(element, argument);
        }
    }

    /**
     * Handle elements of the array.
     *
     * @param <T>           the type parameter
     * @param <F>           the type parameter
     * @param <R>           the type parameter
     * @param array         the array.
     * @param argument      the additional argument.
     * @param getElement    the function to get sub element.
     * @param finalFunction the final function.
     */
    public static <T, F, R> void forEach(@Nullable final T[] array, @Nullable final F argument,
                                         @NotNull final Function<T, R> getElement,
                                         @NotNull final BiConsumer<R, F> finalFunction) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            final R subElement = getElement.apply(element);
            if (subElement != null) finalFunction.accept(subElement, argument);
        }
    }

    /**
     * Apply the function to each filtered element of the array.
     *
     * @param <T>       the type parameter
     * @param <F>       the type parameter
     * @param array     the array.
     * @param argument  the additional argument.
     * @param condition the condition.
     * @param function  the function.
     */
    public static <T, F> void forEach(@Nullable final T[] array, @Nullable final F argument,
                                      @NotNull final Predicate<T> condition, @NotNull final BiConsumer<T, F> function) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            if (condition.test(element)) {
                function.accept(element, argument);
            }
        }
    }

    /**
     * Apply the function to each filtered element of the array.
     *
     * @param <T>           the type parameter
     * @param <R>           the type parameter
     * @param <F>           the type parameter
     * @param array         the array.
     * @param argument      the additional argument.
     * @param condition     the condition.
     * @param getElement    the function to get sub element.
     * @param finalFunction the final function.
     */
    public static <T, R, F> void forEach(@Nullable final T[] array, @Nullable final F argument,
                                         @NotNull final Predicate<T> condition,
                                         @NotNull final Function<T, R> getElement,
                                         @NotNull final BiConsumer<R, F> finalFunction) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            if (!condition.test(element)) continue;
            final R subElement = getElement.apply(element);
            finalFunction.accept(subElement, argument);
        }
    }

    /**
     * Apply the function to each element of the array.
     *
     * @param <T>      the type parameter
     * @param <F>      the type parameter
     * @param <S>      the type parameter
     * @param array    the array.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     */
    public static <T, F, S> void forEach(@Nullable final T[] array, @Nullable final F first, @Nullable final S second,
                                         @NotNull final TripleConsumer<T, F, S> function) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            function.accept(element, first, second);
        }
    }

    /**
     * Apply the function to each sub-element of the array.
     *
     * @param <T>        the type parameter
     * @param <R>        the type parameter
     * @param <F>        the type parameter
     * @param <S>        the type parameter
     * @param array      the array.
     * @param first      the first argument.
     * @param second     the second argument.
     * @param getElement the function to get sub element.
     * @param function   the function.
     */
    public static <T, R, F, S> void forEach(@Nullable final T[] array, @Nullable final F first,
                                            @Nullable final S second,
                                            @NotNull final TripleFunction<T, F, S, R> getElement,
                                            @NotNull final TripleConsumer<R, F, S> function) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            final R subElement = getElement.apply(element, first, second);
            function.accept(subElement, first, second);
        }
    }

    /**
     * Find an index of the element in the array using condition.
     *
     * @param <T>       the type parameter
     * @param <F>       the type parameter
     * @param array     the array.
     * @param argument  the argument.
     * @param condition the condition.
     * @return the index of the element or -1.
     */
    public static <T, F> int indexOf(@Nullable final T[] array, @Nullable final F argument,
                                     @NotNull final BiPredicate<T, F> condition) {
        return indexOf(array, argument, condition, 0, array == null ? 0 : array.length);
    }

    /**
     * Find an index of the element in an array using condition in the range.
     *
     * @param <T>        the type parameter
     * @param <F>        the type parameter
     * @param array      the array.
     * @param argument   the argument.
     * @param condition  the condition.
     * @param startIndex the start index.
     * @param endIndex   the end index.
     * @return the index of the element or -1.
     */
    public static <T, F> int indexOf(@Nullable final T[] array, @Nullable final F argument,
                                     @NotNull final BiPredicate<T, F> condition, final int startIndex,
                                     final int endIndex) {
        if (array == null || array.length < 1) return -1;
        for (int i = startIndex; i < endIndex; i++) {
            if (condition.test(array[i], argument)) return i;
        }
        return -1;
    }

    /**
     * Calculate a count of interesting elements of the array.
     *
     * @param <T>       the type parameter
     * @param array     the array.
     * @param condition the condition.
     * @return the count of elements.
     */
    public static <T> int count(@Nullable final T[] array, @NotNull final Predicate<T> condition) {
        if (array == null || array.length < 1) return 0;

        int count = 0;

        for (final T element : array) {
            if (condition.test(element)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Find an element in the array using the condition.
     *
     * @param <T>       the array's element type.
     * @param array     the array.
     * @param condition the condition.
     * @return the element or null.
     */
    public static <T> @Nullable T findAny(@Nullable T[] array, @NotNull Predicate<? super T> condition) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {
            if (condition.test(element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Return true if there is at least an element for the condition.
     *
     * @param <T>       the array's element type.
     * @param array     the array.
     * @param condition the condition.
     * @return true if there is at least an element for the condition.
     */
    public static <T> boolean anyMatch(@Nullable T[] array, @NotNull Predicate<? super T> condition) {
        return findAny(array, condition) != null;
    }

    /**
     * Find an element in the array using the condition.
     *
     * @param <T>       the array's element type.
     * @param <F>       the argument's type.
     * @param array     the array.
     * @param argument  the argument.
     * @param condition the condition.
     * @return the element or null.
     */
    public static <T, F> @Nullable T findAny(
            @Nullable T[] array,
            @Nullable F argument,
            @NotNull BiPredicate<? super T, F> condition
    ) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {
            if (condition.test(element, argument)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Find an element in the array using the condition.
     *
     * @param <T>       the array's element type.
     * @param <F>       the argument's type.
     * @param array     the array.
     * @param argument  the argument.
     * @param condition the condition.
     * @return the element or null.
     */
    public static <T, F> @Nullable T findAnyR(
            @Nullable T[] array,
            @Nullable F argument,
            @NotNull BiPredicate<F, ? super T> condition
    ) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {
            if (condition.test(argument, element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Return true if there is at least an element for the condition.
     *
     * @param <T>       the array's element type.
     * @param <F>       the argument's type.
     * @param array     the array.
     * @param argument  the argument.
     * @param condition the condition.
     * @return true if there is at least an element for the condition.
     */
    public static <T, F> boolean anyMatchR(
            @Nullable T[] array,
            @Nullable F argument,
            @NotNull BiPredicate<F, ? super T> condition
    ) {
        return findAnyR(array, argument, condition) != null;
    }

    /**
     * Find a sub-element in the array using the function to get a sub-element + the condition.
     *
     * @param <T>        the type parameter
     * @param <R>        the type parameter
     * @param <F>        the type parameter
     * @param array      the array.
     * @param argument   the argument.
     * @param getElement the function to get a sub-element.
     * @param condition  the condition.
     * @return the element or null.
     */
    public static <T, R, F> @Nullable R findAny(
            @Nullable T[] array,
            @Nullable F argument,
            @NotNull Function<T, R> getElement,
            @NotNull BiPredicate<R, F> condition
    ) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {

            R subElement = getElement.apply(element);

            if (condition.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Find a sub-element in the array using the function to get a sub-element + conditions.
     *
     * @param <T>        the type parameter
     * @param <R>        the type parameter
     * @param <F>        the type parameter
     * @param array      the array.
     * @param argument   the argument.
     * @param condition  the condition.
     * @param getElement the function to get a sub-element.
     * @param secondCond the second cond
     * @return the element or null.
     */
    public static <T, R, F> @Nullable R findAny(
            @Nullable T[] array,
            @Nullable F argument,
            @NotNull Predicate<T> condition,
            @NotNull Function<T, R> getElement,
            @NotNull BiPredicate<R, F> secondCond
    ) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {

            if (!condition.test(element)) {
                continue;
            }

            R subElement = getElement.apply(element);

            if (secondCond.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Find an element in the array using the condition.
     *
     * @param <T>       the type parameter
     * @param array     the array.
     * @param argument  the argument.
     * @param condition the condition.
     * @return the element or null.
     */

    public static <T> @Nullable T findAny(@Nullable T[] array, int argument, @NotNull ObjectIntPredicate<T> condition) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {
            if (condition.test(element, argument)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Find a sub-element in the array using the function to get a sub-element + the condition.
     *
     * @param <T>        the type parameter
     * @param <R>        the type parameter
     * @param array      the array.
     * @param argument   the argument.
     * @param getElement the function to get a sub-element.
     * @param condition  the condition.
     * @return the element or null.
     */

    public static <T, R> @Nullable R findAny(
            @Nullable T[] array,
            int argument,
            @NotNull Function<T, R> getElement,
            @NotNull ObjectIntPredicate<R> condition
    ) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {
            R subElement = getElement.apply(element);
            if (condition.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Find a sub-element in the array using the function to get a sub-element + conditions.
     *
     * @param <T>        the type parameter
     * @param <R>        the type parameter
     * @param array      the array.
     * @param argument   the argument.
     * @param firstCond  the first condition.
     * @param getElement the function to get a sub-element.
     * @param secondCond the second condition.
     * @return the element or null.
     */

    public static <T, R> @Nullable R findAny(
            @Nullable T[] array,
            int argument,
            @NotNull Predicate<T> firstCond,
            @NotNull Function<T, R> getElement,
            @NotNull ObjectIntPredicate<R> secondCond
    ) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {

            if (!firstCond.test(element)) {
                continue;
            }

            R subElement = getElement.apply(element);

            if (secondCond.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Find a sub-element in the array using the function to get a sub-element + conditions.
     *
     * @param <T>        the type parameter
     * @param <R>        the type parameter
     * @param array      the array.
     * @param argument   the argument.
     * @param firstCond  the first condition.
     * @param getElement the function to get a sub-element.
     * @param secondCond the second condition.
     * @return the element or null.
     */
    public static <T, R> @Nullable R findL(
            @Nullable T[] array,
            long argument,
            @NotNull Predicate<T> firstCond,
            @NotNull Function<T, R> getElement,
            @NotNull ObjectLongPredicate<R> secondCond
    ) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {

            if (!firstCond.test(element)) {
                continue;
            }

            R subElement = getElement.apply(element);

            if (secondCond.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Find an element in the array using the condition.
     *
     * @param <T>       the type parameter
     * @param array     the array.
     * @param argument  the argument.
     * @param condition the condition.
     * @return the element or null.
     */
    public static <T> @Nullable T findL(@Nullable final T[] array, final long argument,
                                        @NotNull final ObjectLongPredicate<T> condition) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (final T element : array) {
            if (condition.test(element, argument)) return element;
        }

        return null;
    }

    /**
     * Find an element in the array using the condition.
     *
     * @param <T>        the type parameter
     * @param <R>        the type parameter
     * @param array      the array.
     * @param argument   the argument.
     * @param getElement the get element
     * @param condition  the condition.
     * @return the element or null.
     */
    public static <T, R> @Nullable R findL(@Nullable final T[] array, final long argument,
                                           @NotNull final Function<T, R> getElement,
                                           @NotNull final ObjectLongPredicate<R> condition) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (final T element : array) {
            final R subElement = getElement.apply(element);
            if (condition.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Find an element in the array using the condition.
     *
     * @param <T>       the type parameter
     * @param <F>       the type parameter
     * @param <S>       the type parameter
     * @param array     the array.
     * @param first     the first argument.
     * @param second    the second argument.
     * @param condition the condition.
     * @return the element or null.
     */
    public static <T, F, S> @Nullable T findAny(
            @Nullable T[] array,
            @Nullable F first,
            @Nullable S second,
            @NotNull TriplePredicate<T, F, S> condition
    ) {

        if (array == null || array.length < 1) {
            return null;
        }

        for (T element : array) {
            if (condition.test(element, first, second)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Return true if the array is null or empty.
     *
     * @param array the array.
     * @return true if the array is null or empty.
     */
    public static boolean isEmpty(@Nullable byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Return true if the array is null or empty.
     *
     * @param array the array.
     * @return true if the array is null or empty.
     */
    public static boolean isEmpty(@Nullable short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Return true if the array is null or empty.
     *
     * @param array the array.
     * @return true if the array is null or empty.
     */
    public static boolean isEmpty(@Nullable char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Return true if the array is null or empty.
     *
     * @param array the array.
     * @return true if the array is null or empty.
     */
    public static boolean isEmpty(@Nullable int[] array) {
        return array == null || array.length == 0;
    }


    /**
     * Return true if the array is null or empty.
     *
     * @param array the array.
     * @return true if the array is null or empty.
     */
    public static boolean isEmpty(@Nullable long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Return true if the array is null or empty.
     *
     * @param array the array.
     * @return true if the array is null or empty.
     */
    public static boolean isEmpty(@Nullable float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Return true if the array is null or empty.
     *
     * @param array the array.
     * @return true if the array is null or empty.
     */
    public static boolean isEmpty(@Nullable double[] array) {
        return array == null || array.length == 0;
    }

    private ArrayUtils() {
        throw new RuntimeException();
    }
}
