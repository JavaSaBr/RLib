package com.ss.rlib.common.util;

import com.ss.rlib.common.function.*;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.IntegerArray;
import com.ss.rlib.common.util.array.LongArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
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
     * Convert an object integer array to primitive int array.
     *
     * @param array the object integer array.
     * @return the primitive int array.
     * @since 9.2.1
     */
    public static @NotNull int[] toIntArray(@NotNull Integer[] array) {

        if (array.length < 1) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }

        var intArray = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            intArray[i] = array[i];
        }

        return intArray;
    }

    /**
     * Convert an string to primitive int array by regex.
     *
     * @param string the string.
     * @param regex  the regex.
     * @return the primitive int array.
     * @throws NumberFormatException if some elements in the string are not an integer.
     * @since 9.2.1
     */
    public static @NotNull int[] toIntArray(@NotNull String string, @NotNull String regex) {

        if (string.isBlank()) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }

        var elements = string.split(regex);

        if (elements.length < 1) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }

        var intArray = new int[elements.length];

        for (int i = 0; i < elements.length; i++) {
            intArray[i] = Integer.parseInt(elements[i].trim());
        }

        return intArray;
    }

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

        array = copyOfAndExtend(array, 1);
        array[length] = element;

        return array;
    }

    /**
     * Clear all elements in the array.
     *
     * @param array the array.
     */
    public static void clear(@NotNull Object[] array) {
        Arrays.fill(array, null);
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
        Arrays.setAll(array, factory);
    }

    /**
     * Fill the array using the factory.
     *
     * @param <T>      the element's type.
     * @param <F>      the argument's type.
     * @param array    the array.
     * @param argument the additional argument.
     * @param factory  the element's factory.
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
     * Combine two arrays to one single array.
     *
     * @param <T>   the base array's component type.
     * @param <E>   the added array's component type.
     * @param base  the base array.
     * @param added the additional array.
     * @return the combined array.
     */
    public static <T, E extends T> @NotNull T[] combine(@NotNull T[] base, @Nullable E[] added) {
        return combine(base, added, getComponentType(base));
    }

    /**
     * Combine two arrays to one single array.
     *
     * @param <T>   the base array's component type.
     * @param <E>   the added array's component type.
     * @param base  the base array.
     * @param added the additional array.
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
    public static boolean contains(@NotNull int[] array, int val) {

        for (int value : array) {
            if (value == val) {
                return true;
            }
        }

        return false;
    }

    /**
     * Return true if an array contains an object.
     *
     * @param array  the array.
     * @param object the object.
     * @return true if the array contains the object.
     */
    public static boolean contains(@NotNull Object[] array, @NotNull Object object) {

        for (var element : array) {
            if (Objects.equals(element, object)) {
                return true;
            }
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
        var copy = new byte[old.length + added];
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
        var copy = new int[old.length + added];
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
        var copy = new long[old.length + added];
        System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));
        return copy;
    }

    /**
     * Copy a native array.
     *
     * @param <T>      the array component's type.
     * @param original the original array.
     * @return the new copied native array.
     */
    public static <T> @NotNull T[] copyOf(@NotNull T[] original) {
        return Arrays.copyOf(original, original.length);
    }

    /**
     * Copy an array and extend if need.
     *
     * @param <T>      the array component's type.
     * @param original the original array.
     * @param added    the additional size.
     * @return the new copied array.
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull T[] copyOfAndExtend(@NotNull T[] original, int added) {
        return Arrays.copyOf(original, original.length + added);
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
    public static void copyTo(@NotNull int[] source, int[] target) {
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
    public static void copyTo(
        @NotNull int[] source,
        @NotNull int[] target,
        int sourceOffset,
        int targetOffset,
        int length
    ) {
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
    public static @NotNull int[] copyOfRange(@NotNull int[] original, int from, int to) {

        var newLength = to - from;
        var copy = new int[newLength];

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
    public static @NotNull long[] copyOfRange(@NotNull long[] original, int from, int to) {

        var newLength = to - from;
        var copy = new long[newLength];

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
    public static <T> @NotNull T[] copyOfRange(@NotNull T[] original, int from, int to) {
        return Arrays.copyOfRange(original, from, to);
    }

    /**
     * Get a component type of an array.
     *
     * @param <T>     the array's component type.
     * @param example the array's example.
     * @return the component type.
     */
    public static <T> @NotNull Class<T> getComponentType(@NotNull T[] example) {
        return ClassUtils.unsafeNNCast(example.getClass().getComponentType());
    }

    /**
     * Create the array by the type.
     *
     * @param <T>     the type parameter
     * @param example the array's example.
     * @param size    the size.
     * @return the new array.
     */
    public static <T> @NotNull T[] create(@NotNull T[] example, int size) {
        return ClassUtils.unsafeCast(java.lang.reflect.Array.newInstance(example.getClass().getComponentType(), size));
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
    public static int indexOf(@NotNull Object[] array, @Nullable Object object) {

        int index = 0;

        for (var element : array) {
            if (Objects.equals(element, object)) {
                return index;
            }
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
     * @param <T>      the element's type.
     * @param array    the array.
     * @param toString the to string function.
     * @return the string presentation.
     */
    public static <T> @NotNull String toString(@NotNull Array<T> array, @NotNull Function<T, String> toString) {

        if (array.isEmpty()) {
            return "[]";
        }

        var className = array.array()
            .getClass()
            .getSimpleName();

        var builder = new StringBuilder(className.substring(0, className.length() - 1));

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
     * Return true if the array is not null or empty.
     *
     * @param array the array.
     * @return true if the array is not null or empty.
     */
    public static boolean isNotEmpty(@Nullable byte[] array) {
        return array != null && array.length > 0;
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
     * Return true if the array is not null or empty.
     *
     * @param array the array.
     * @return true if the array is not null or empty.
     */
    public static boolean isNotEmpty(@Nullable short[] array) {
        return array != null && array.length > 0;
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
     * Return true if the array is not null or empty.
     *
     * @param array the array.
     * @return true if the array is not null or empty.
     */
    public static boolean isNotEmpty(@Nullable char[] array) {
        return array != null && array.length > 0;
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
     * Return true if the array is not null or empty.
     *
     * @param array the array.
     * @return true if the array is not null or empty.
     */
    public static boolean isNotEmpty(@Nullable int[] array) {
        return array != null && array.length > 0;
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
     * Return true if the array is not null or empty.
     *
     * @param array the array.
     * @return true if the array is not null or empty.
     */
    public static boolean isNotEmpty(@Nullable long[] array) {
        return array != null && array.length > 0;
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
     * Return true if the array is not null or empty.
     *
     * @param array the array.
     * @return true if the array is not null or empty.
     */
    public static boolean isNotEmpty(@Nullable float[] array) {
        return array != null && array.length > 0;
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
     * Return true if the array is not null or empty.
     *
     * @param array the array.
     * @return true if the array is not null or empty.
     */
    public static boolean isNotEmpty(@Nullable double[] array) {
        return array != null && array.length > 0;
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

    /**
     * Return true if the array is not null or empty.
     *
     * @param array the array.
     * @return true if the array is not null or empty.
     */
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return array != null && array.length > 0;
    }

    /**
     * Return true if the array is null or empty.
     *
     * @param array the array.
     * @return true if the array is null or empty.
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Convert T array to R array.
     *
     * @param <T>        the source component type.
     * @param <M>        the mapped element's type.
     * @param <R>        the result element's type.
     * @param source     the source array.
     * @param mapper     the mapper.
     * @param resultType the result component type.
     * @return the mapped array.
     * @since 9.5.0
     */
    public static <T, R, M extends R> R @Nullable [] map(
        T @NotNull [] source,
        @NotNull Function<@NotNull T, @NotNull M> mapper,
        @NotNull Class<R> resultType
    ) {

        if (source.length == 0) {
            return create(resultType, 0);
        }

        R[] resultArray = create(resultType, source.length);

        for (int i = 0; i < source.length; i++) {
            resultArray[i] = mapper.apply(source[i]);
        }

        return resultArray;
    }

    /**
     * Convert T array to R array.
     *
     * @param <T>    the source component type.
     * @param <M>    the mapped element's type.
     * @param <R>    the result element's type.
     * @param source the source array.
     * @param mapper the mapper.
     * @param def    the default result if source array is null.
     * @return the mapped array.
     * @since 9.5.0
     */
    public static <T, R, M extends R> R @NotNull [] map(
        T @Nullable [] source,
        @NotNull Function<@NotNull T, @NotNull M> mapper,
        @NotNull R[] def
    ) {

        if (source == null || source.length == 0) {
            return def;
        }

        R[] resultArray = create(def.getClass().getComponentType(), source.length);

        for (int i = 0; i < source.length; i++) {
            resultArray[i] = mapper.apply(source[i]);
        }

        return resultArray;
    }

    /**
     * Convert T array to R array if a source array is not null.
     *
     * @param <T>        the source component type.
     * @param <M>        the mapped element's type.
     * @param <R>        the result element's type.
     * @param source     the source array.
     * @param mapper     the mapper.
     * @param resultType the result component type.
     * @return the mapped array or null.
     * @since 9.3.0
     */
    public static <T, R, M extends R> R @Nullable [] mapNullable(
        T @Nullable [] source,
        @NotNull Function<@NotNull T, @NotNull M> mapper,
        @NotNull Class<R> resultType
    ) {

        if (source == null) {
            return null;
        } else if (source.length == 0) {
            return create(resultType, 0);
        }

        R[] resultArray = create(resultType, source.length);

        for (int i = 0; i < source.length; i++) {
            resultArray[i] = mapper.apply(source[i]);
        }

        return resultArray;
    }

    /**
     * Convert long array to int array.
     *
     * @param source the source array.
     * @return the int array.
     * @since 9.3.0
     */
    public static int @NotNull [] longsToInts(long @NotNull [] source) {

        if (source.length == 0) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }

        var resultArray = new int[source.length];

        for (int i = 0; i < source.length; i++) {
            resultArray[i] = (int) source[i];
        }

        return resultArray;
    }

    private ArrayUtils() {
        throw new RuntimeException();
    }
}
