package rlib.util.array;

/**
 * Реализация фабрики различных массивов.
 * 
 * @author Ronn
 */
public class ArrayFactory {

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
	 * Создать быстрый новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newArray(Class<?> type) {
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
	public static <E> Array<E> newArray(Class<?> type, int size) {
		return new FastArray<E>((Class<E>) type, size);
	}

	/**
	 * Создать уникальное множество на основе быстрого массива.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newArraySet(Class<?> type) {
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
	public static <E> Array<E> newArraySet(Class<?> type, int size) {
		return new FastArraySet<E>((Class<E>) type, size);
	}

	/**
	 * Создать потокобезопасный новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newConcurrentArray(Class<?> type) {
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
	public static <E> Array<E> newConcurrentArray(Class<?> type, int size) {
		return new ConcurrentArray<E>((Class<E>) type, size);
	}

	/**
	 * Создать уникальное множество на основе конкурентного массива.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newConcurrentArraySet(Class<?> type) {
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
	public static <E> Array<E> newConcurrentArraySet(Class<?> type, int size) {
		return new ConcurrentArraySet<E>((Class<E>) type, size);
	}

	/**
	 * Создает массив из перечисленных чисел.
	 * 
	 * @param elements набор чисел.
	 * @return новый массив.
	 */
	public static float[] newFloatArray(float... elements) {
		return elements;
	}

	/**
	 * Создает массив из перечисленных элементов.
	 * 
	 * @param elements набор элементов.
	 * @return новый массив.
	 */
	@SafeVarargs
	public static <T, K extends T> T[] newGenericArray(K... elements) {
		return elements;
	}

	/**
	 * Создает массив из перечисленных чисел.
	 * 
	 * @param elements набор чисел.
	 * @return новый массив.
	 */
	public static int[] newIntegerArray(int... elements) {
		return elements;
	}

	/**
	 * Создать сортируемый новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Comparable<E>> Array<E> newSortedArray(Class<?> type) {
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
	public static <E extends Comparable<E>> Array<E> newSortedArray(Class<?> type, int size) {
		return new SortedArray<E>((Class<E>) type, size);
	}

	/**
	 * Создать синхронизированный новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newSynchronizedArray(Class<?> type) {
		return new SynchronizedArray<E>((Class<E>) type);
	}
}
