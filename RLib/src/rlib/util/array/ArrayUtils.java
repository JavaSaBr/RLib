package rlib.util.array;

import java.util.Comparator;

import rlib.util.Objects;

/**
 * Набор утильных методов для работы с массивами.
 * 
 * @author Ronn
 * @created 07.04.2012
 */
public final class ArrayUtils {

	/**
	 * Добавляет элемент в массив с расширением массива на +1.
	 * 
	 * @param array исходный массив.
	 * @param element добавляемый элемент.
	 * @param type тип массива.
	 * @return новый массив с указанным элементом в конце.
	 */
	public static <T> T[] addToArray(T[] array, T element, Class<T> type) {

		if(array == null) {
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
	 * Зануление всех элементов массива.
	 * 
	 * @param array массив, элементы которого нужно занулить.
	 */
	public static void clear(Object[] array) {
		for(int i = 0, length = array.length; i < length; i++) {
			array[i] = null;
		}
	}

	/**
	 * Совместить 2 массива в один.
	 * 
	 * @param base исходный массив.
	 * @param added добавочный массив.
	 * @return новый общий массив.
	 */
	public static int[] combine(int[] base, int[] added) {

		if(base == null) {
			return added;
		}

		if(added == null || added.length < 1) {
			return base;
		}

		int[] result = new int[base.length + added.length];

		int index = 0;

		for(int i = 0, length = base.length; i < length; i++) {
			result[index++] = base[i];
		}

		for(int i = 0, length = added.length; i < length; i++) {
			result[index++] = added[i];
		}

		return result;
	}

	/**
	 * Совмещение 2х масивов в 1.
	 * 
	 * @param base базовый массив.
	 * @param added добавляемый массив.
	 * @param type тип массива.
	 * @return новый массив.
	 */
	public static <T, E extends T> T[] combine(T[] base, E[] added, Class<T> type) {

		if(base == null) {
			return added;
		}

		if(added == null || added.length < 1) {
			return base;
		}

		T[] result = create(type, base.length + added.length);

		int index = 0;

		for(int i = 0, length = base.length; i < length; i++) {
			result[index++] = base[i];
		}

		for(int i = 0, length = added.length; i < length; i++) {
			result[index++] = added[i];
		}

		return result;
	}

	/**
	 * Проверка на содержания в массиве указанного значения.
	 * 
	 * @param array проверяемый массив.
	 * @param val искомое значение.
	 * @return содержит ли массив указанное значение.
	 */
	public static boolean contains(int[] array, int val) {

		for(int value : array) {
			if(value == val) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Проверка на содержания в массиве указанного значения.
	 * 
	 * @param array проверяемый массив.
	 * @param object искомое значение.
	 * @return содержит ли массив указанное значение.
	 */
	public static boolean contains(Object[] array, Object object) {

		for(Object element : array) {
			if(Objects.equals(element, object)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Копирование массива с созданием нового на указанный размер больше.
	 * 
	 * @param old исходный массив.
	 * @param added сила расширения.
	 * @return новый массив.
	 */
	public static byte[] copyOf(byte[] old, int added) {

		byte[] copy = new byte[old.length + added];

		System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));

		return copy;
	}

	/**
	 * Копирование массива с созданием нового на указанный размер больше.
	 * 
	 * @param old исходный массив.
	 * @param added сила расширения.
	 * @return новый массив.
	 */
	public static int[] copyOf(int[] old, int added) {

		int[] copy = new int[old.length + added];

		System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));

		return copy;
	}

	/**
	 * Копирование массива с созданием нового на указанный размер больше.
	 * 
	 * @param old исходный массив.
	 * @param added сила расширения.
	 * @return новый массив.
	 */
	public static long[] copyOf(long[] old, int added) {

		long[] copy = new long[old.length + added];

		System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));

		return copy;
	}

	/**
	 * Копирование массива с созданием нового на указанный размер больше.
	 * 
	 * @param old исходный массив.
	 * @param added сила расширения.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] copyOf(T[] old, int added) {

		Class<? extends Object[]> newType = old.getClass();

		T[] copy = (T[]) create(newType.getComponentType(), old.length + added);

		System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));

		return copy;
	}

	/**
	 * Копируем часть из массива и создаем новый массив из этой части.
	 * 
	 * @param original исходный массив.
	 * @param from с какого индекса.
	 * @param to по какой индекс.
	 * @return новый массив.
	 */
	public static int[] copyOfRange(int[] original, int from, int to) {

		int newLength = to - from;

		int[] copy = new int[newLength];

		System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));

		return copy;
	}

	/**
	 * Копируем часть из массива и создаем новый массив из этой части.
	 * 
	 * @param original исходный массив.
	 * @param from с какого индекса.
	 * @param to по какой индекс.
	 * @return новый массив.
	 */
	public static long[] copyOfRange(long[] original, int from, int to) {

		int newLength = to - from;

		long[] copy = new long[newLength];

		System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));

		return copy;
	}

	/**
	 * Копируем часть из массива и создаем новый массив из этой части.
	 * 
	 * @param original исходный массив.
	 * @param from с какого индекса.
	 * @param to по какой индекс.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] copyOfRange(T[] original, int from, int to) {

		Class<? extends Object[]> newType = original.getClass();

		int newLength = to - from;

		T[] copy = (T[]) create(newType.getComponentType(), newLength);

		System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));

		return copy;
	}

	/**
	 * Создание массива указанного типа.
	 * 
	 * @param type тир массива.
	 * @param size размер массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] create(Class<?> type, int size) {
		return (T[]) java.lang.reflect.Array.newInstance(type, size);
	}

	/**
	 * Находит индекс объекта в указанном массиве.
	 * 
	 * @param array массив.
	 * @param object искомый объект.
	 * @return индекс оюъекта.
	 */
	public static int indexOf(Object[] array, Object object) {

		int index = 0;

		for(Object element : array) {

			if(Objects.equals(element, object)) {
				return index;
			}

			index++;
		}

		return -1;
	}

	/**
	 * @return новый динамический массив примтивного инт.
	 */
	public static IntegerArray newIntegerArray() {
		return new FastIntegerArray();
	}

	/**
	 * @param size начальный размер массива.
	 * @return новый динамический массив примтивного инт.
	 */
	public static IntegerArray newIntegerArray(int size) {
		return new FastIntegerArray(size);
	}

	/**
	 * @return новый динамический массив примтивного инт.
	 */
	public static LongArray newLongArray() {
		return new FastLongArray();
	}

	/**
	 * @param size начальный размер массива.
	 * @return новый динамический массив примтивного инт.
	 */
	public static LongArray newLongArray(int size) {
		return new FastLongArray(size);
	}

	/**
	 * Сортировка массива, недопустимы нулевые значения.
	 * 
	 * @param array сортируемый массив.
	 */
	public static void sort(Comparable<?>[] array) {
		java.util.Arrays.sort(array);
	}

	/**
	 * Сортировка массива, недопустимы нулевые значения.
	 * 
	 * @param array сортируемый массив.
	 */
	public static void sort(int[] array) {
		java.util.Arrays.sort(array);
	}

	/**
	 * Сортировка массива, недопустимы нулевые значения.
	 * 
	 * @param array сортируемый массив.
	 */
	public static void sort(int[] array, int fromIndex, int toIndex) {
		java.util.Arrays.sort(array, fromIndex, toIndex);
	}

	/**
	 * Сортировка массива, недопустимы нулевые значения.
	 * 
	 * @param array сортируемый массив.
	 */
	public static void sort(long[] array, int fromIndex, int toIndex) {
		java.util.Arrays.sort(array, fromIndex, toIndex);
	}

	/**
	 * Сортировка массива компаратором.
	 * 
	 * @param array сортируемый массив.
	 * @param comparator компаратор для массива.
	 */
	public static <T> void sort(T[] array, Comparator<? super T> comparator) {
		java.util.Arrays.sort(array, comparator);
	}

	/**
	 * Создать быстрый новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> toArray(Class<?> type) {
		return new FastArray<E>((Class<E>) type);
	}

	/**
	 * Создать быстрый новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @param size базовый размер массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> toArray(Class<?> type, int size) {
		return new FastArray<E>((Class<E>) type, size);
	}

	/**
	 * Создать уникальное множество на основе быстрого массива.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> toArraySet(Class<?> type) {
		return new FastArraySet<E>((Class<E>) type);
	}

	/**
	 * Создать уникальное множество на основе быстрого массива.
	 * 
	 * @param type тип массива.
	 * @param size базовый размер массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> toArraySet(Class<?> type, int size) {
		return new FastArraySet<E>((Class<E>) type, size);
	}

	/**
	 * Создать потокобезопасный новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> toConcurrentArray(Class<?> type) {
		return new ConcurrentArray<E>((Class<E>) type);
	}

	/**
	 * Создать потокобезопасный новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @param size базовый размер массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> toConcurrentArray(Class<?> type, int size) {
		return new ConcurrentArray<E>((Class<E>) type, size);
	}

	/**
	 * Создать уникальное множество на основе конкурентного массива.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> toConcurrentArraySet(Class<?> type) {
		return new ConcurrentArraySet<E>((Class<E>) type);
	}

	/**
	 * Создать уникальное множество на основе конкурентного массива.
	 * 
	 * @param type тип массива.
	 * @param size базовый размер массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> toConcurrentArraySet(Class<?> type, int size) {
		return new ConcurrentArraySet<E>((Class<E>) type, size);
	}

	/**
	 * Создает массив из перечисленных чисел.
	 * 
	 * @param elements набор чисел.
	 * @return новый массив.
	 */
	public static float[] toFloatArray(float... elements) {
		return elements;
	}

	/**
	 * Создает массив из перечисленных элементов.
	 * 
	 * @param elements набор элементов.
	 * @return новый массив.
	 */
	@SafeVarargs
	public static <T, K extends T> T[] toGenericArray(K... elements) {
		return elements;
	}

	/**
	 * Создает массив из перечисленных чисел.
	 * 
	 * @param elements набор чисел.
	 * @return новый массив.
	 */
	public static int[] toIntegerArray(int... elements) {
		return elements;
	}

	/**
	 * Создать сортируемый новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Comparable<E>> Array<E> toSortedArray(Class<?> type) {
		return new SortedArray<E>((Class<E>) type);
	}

	/**
	 * Создать сортируемый новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @param size базовый размер массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Comparable<E>> Array<E> toSortedArray(Class<?> type, int size) {
		return new SortedArray<E>((Class<E>) type, size);
	}

	/**
	 * Конфектирует массив объектов строку.
	 * 
	 * @param array массив объектов.
	 * @return строковый вариант.
	 */
	public static String toString(Array<?> array) {

		if(array == null) {
			return "[]";
		}

		String className = array.array().getClass().getSimpleName();

		StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

		for(int i = 0, length = array.size() - 1; i <= length; i++) {

			builder.append(String.valueOf(array.get(i)));

			if(i == length) {
				break;
			}

			builder.append(", ");
		}

		builder.append("]");
		return builder.toString();
	}

	/**
	 * Конфектирует массив объектов строку.
	 * 
	 * @param array массив объектов.
	 * @return строковый вариант.
	 */
	public static String toString(IntegerArray array) {

		if(array == null) {
			return "[]";
		}

		String className = array.array().getClass().getSimpleName();
		StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

		for(int i = 0, length = array.size() - 1; i <= length; i++) {

			builder.append(String.valueOf(array.get(i)));

			if(i == length) {
				break;
			}

			builder.append(", ");
		}

		builder.append("]");
		return builder.toString();
	}

	/**
	 * Конфектирует массив объектов строку.
	 * 
	 * @param array массив объектов.
	 * @return строковый вариант.
	 */
	public static String toString(LongArray array) {

		if(array == null) {
			return "[]";
		}

		String className = array.array().getClass().getSimpleName();
		StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

		for(int i = 0, length = array.size() - 1; i <= length; i++) {

			builder.append(String.valueOf(array.get(i)));

			if(i == length) {
				break;
			}

			builder.append(", ");
		}

		builder.append("]");
		return builder.toString();
	}

	/**
	 * Конфектирует массив объектов строку.
	 * 
	 * @param array массив объектов.
	 * @return строковый вариант.
	 */
	public static String toString(Object[] array) {

		if(array == null) {
			return "[]";
		}

		String className = array.getClass().getSimpleName();
		StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

		for(int i = 0, length = array.length - 1; i <= length; i++) {

			builder.append(String.valueOf(array[i]));

			if(i == length) {
				break;
			}

			builder.append(", ");
		}

		builder.append("]");
		return builder.toString();
	}

	/**
	 * Создать синхронизированный новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> toSynchronizedArray(Class<?> type) {
		return new SynchronizedArray<E>((Class<E>) type);
	}
}
