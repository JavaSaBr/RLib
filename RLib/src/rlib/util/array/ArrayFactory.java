package rlib.util.array;

import rlib.util.array.impl.ConcurrentArray;
import rlib.util.array.impl.ConcurrentArraySet;
import rlib.util.array.impl.ConcurrentAtomicArray;
import rlib.util.array.impl.FastArray;
import rlib.util.array.impl.FastArraySet;
import rlib.util.array.impl.FastIntegerArray;
import rlib.util.array.impl.FastLongArray;
import rlib.util.array.impl.SortedArray;
import rlib.util.array.impl.SynchronizedArray;

/**
 * Реализация фабрики различных массивов.
 * 
 * @author Ronn
 */
public class ArrayFactory {

	/**
	 * Создание нового не потокобезопасного динамического массива.
	 * 
	 * @see FastArray
	 * @param type тип элементов в массиве.
	 * @return новый экземпляр массива.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newArray(final Class<?> type) {
		return new FastArray<E>((Class<E>) type);
	}

	/**
	 * Создание нового не потокобезопасного динамического массива с проверкой на
	 * уникальность элементов при вставке.
	 * 
	 * @see FastArraySet
	 * @param type тип элементов в массиве.
	 * @return новый экземпляр массива.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newArraySet(final Class<?> type) {
		return new FastArraySet<E>((Class<E>) type);
	}

	/**
	 * Создание нового потокобезопасного динамического массива с возможностью
	 * синхронно записывать и асинхронно читать.
	 * 
	 * @see ConcurrentArray
	 * @param type тип элементов в массиве.
	 * @return новый экземпляр массива.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newConcurrentArray(final Class<?> type) {
		return new ConcurrentArray<E>((Class<E>) type);
	}

	/**
	 * Создание нового потокобезопасного динамического массива с проверкой на
	 * уникальность элемента при вставке и с возможностью синхронно записывать и
	 * асинхронно читать.
	 * 
	 * @see ConcurrentArraySet
	 * @param type тип элементов в массиве.
	 * @return новый экземпляр массива.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newConcurrentArraySet(final Class<?> type) {
		return new ConcurrentArraySet<E>((Class<E>) type);
	}

	/**
	 * Создание нового потокобезопасного динамического массива с возможностью
	 * синхронно записывать и асинхронно читать.
	 * 
	 * @see ConcurrentAtomicArray
	 * @param type тип элементов в массиве.
	 * @return новый экземпляр массива.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newConcurrentAtomicArray(final Class<?> type) {
		return new ConcurrentAtomicArray<E>((Class<E>) type);
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
	 * Создает массив из перечисленных элементов.
	 * 
	 * @param elements набор элементов.
	 * @return новый массив.
	 */
	@SafeVarargs
	public static <T, K extends T> T[] newGenericArray(final K... elements) {
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
	 * Создание нового не потокобезопасного сортированного динамического
	 * массива.
	 * 
	 * @see SortedArray
	 * @param type тип элементов массива.
	 * @return новый экземпляр массива.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Comparable<E>> Array<E> newSortedArray(final Class<?> type) {
		return new SortedArray<E>((Class<E>) type);
	}

	/**
	 * Создание нового синхронизированного динамического массива с
	 * синхронизированной записью.
	 * 
	 * @see SynchronizedArray
	 * @param type тип элементов в массиве.
	 * @return новый экземпляр массива.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newSynchronizedArray(final Class<?> type) {
		return new SynchronizedArray<E>((Class<E>) type);
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
	public static <T, K extends T> T[] toGenericArray(final K... elements) {
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
