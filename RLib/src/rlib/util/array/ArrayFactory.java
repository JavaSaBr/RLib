package rlib.util.array;

import rlib.util.array.impl.ConcurrentArray;
import rlib.util.array.impl.ConcurrentArraySet;
import rlib.util.array.impl.ConcurrentAtomicArray;
import rlib.util.array.impl.FastArray;
import rlib.util.array.impl.FastArraySet;
import rlib.util.array.impl.FastIntegerArray;
import rlib.util.array.impl.FastLongArray;
import rlib.util.array.impl.FinalConcurrentAtomicArray;
import rlib.util.array.impl.FinalFastArray;
import rlib.util.array.impl.FinalFastArraySet;
import rlib.util.array.impl.SortedArray;
import rlib.util.array.impl.SynchronizedArray;

import static rlib.util.ClassUtils.unsafeCast;

/**
 * Реализация фабрики различных массивов.
 *
 * @author JavaSaBr
 */
public class ArrayFactory {

    /**
     * Создание нового не потокобезопасного динамического массива.
     *
     * @param type тип элементов в массиве.
     * @return новый экземпляр массива.
     * @see FastArray
     */
    public static <E> Array<E> newArray(final Class<?> type) {
        return new FinalFastArray<>(unsafeCast(type));
    }

    /**
     * Создание нового не потокобезопасного динамического массива.
     *
     * @param type     тип элементов в массиве.
     * @param capacity стартовый размер массива.
     * @return новый экземпляр массива.
     * @see FastArray
     */
    public static <E> Array<E> newArray(final Class<?> type, final int capacity) {
        return new FinalFastArray<>(unsafeCast(type), capacity);
    }

    /**
     * Создание нового не потокобезопасного динамического массива с проверкой на уникальность
     * элементов при вставке.
     *
     * @param type тип элементов в массиве.
     * @return новый экземпляр массива.
     * @see FastArraySet
     */
    public static <E> Array<E> newArraySet(final Class<?> type) {
        return new FinalFastArraySet<>(unsafeCast(type));
    }

    /**
     * Создание нового потокобезопасного динамического массива с возможностью синхронно записывать и
     * асинхронно читать.
     *
     * @param type тип элементов в массиве.
     * @return новый экземпляр массива.
     * @see ConcurrentArray
     */
    public static <E> Array<E> newConcurrentArray(final Class<?> type) {
        return new ConcurrentArray<>(unsafeCast(type));
    }

    /**
     * Создание нового потокобезопасного динамического массива с проверкой на уникальность элемента
     * при вставке и с возможностью синхронно записывать и асинхронно читать.
     *
     * @param type тип элементов в массиве.
     * @return новый экземпляр массива.
     * @see ConcurrentArraySet
     */
    public static <E> Array<E> newConcurrentArraySet(final Class<?> type) {
        return new ConcurrentArraySet<>(unsafeCast(type));
    }

    /**
     * Создание нового потокобезопасного динамического массива с возможностью синхронно записывать и
     * асинхронно читать.
     *
     * @param type тип элементов в массиве.
     * @return новый экземпляр массива.
     * @see ConcurrentAtomicArray
     */
    public static <E> Array<E> newConcurrentAtomicArray(final Class<?> type) {
        return new FinalConcurrentAtomicArray<>(unsafeCast(type));
    }

    /**
     * Создает массив из перечисленных чисел.
     *
     * @param elements набор чисел.
     * @return новый массив.
     */
    public static float[] newFloatArray(final float... elements) {
        return elements;
    }

    /**
     * Создание не потокобезопасного динамического массива примитивов int.
     *
     * @return новый экземпляр массива.
     */
    public static IntegerArray newIntegerArray() {
        return new FastIntegerArray();
    }

    /**
     * Создает массив из перечисленных чисел.
     *
     * @param elements набор чисел.
     * @return новый массив.
     */
    public static int[] newIntegerArray(final int... elements) {
        return elements;
    }

    /**
     * Создание не потокобезопасного динамического массива примитивов long.
     *
     * @return новый экземпляр массива.
     */
    public static LongArray newLongArray() {
        return new FastLongArray();
    }

    /**
     * Создание нового не потокобезопасного сортированного динамического массива.
     *
     * @param type тип элементов массива.
     * @return новый экземпляр массива.
     * @see SortedArray
     */
    public static <E extends Comparable<E>> Array<E> newSortedArray(final Class<?> type) {
        return new SortedArray<>(unsafeCast(type));
    }

    /**
     * Создание нового синхронизированного динамического массива с синхронизированной записью.
     *
     * @param type тип элементов в массиве.
     * @return новый экземпляр массива.
     * @see SynchronizedArray
     */
    public static <E> Array<E> newSynchronizedArray(final Class<?> type) {
        return new SynchronizedArray<>(unsafeCast(type));
    }

    /**
     * Создает массив из перечисленных чисел.
     *
     * @param elements набор чисел.
     * @return новый массив.
     */
    public static float[] toFloatArray(final float... elements) {
        return elements;
    }

    /**
     * Создает массив из перечисленных элементов.
     *
     * @param elements набор элементов.
     * @return новый массив.
     */
    @SafeVarargs
    public static <T, K extends T> T[] toArray(final K... elements) {
        return elements;
    }

    /**
     * Создает массив из перечисленных чисел.
     *
     * @param elements набор чисел.
     * @return новый массив.
     */
    public static int[] toIntegerArray(final int... elements) {
        return elements;
    }
}
