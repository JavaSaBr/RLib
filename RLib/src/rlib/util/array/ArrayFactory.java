package rlib.util.array;

/**
 * Реализация фабрики различных массивов.
 * 
 * @author Ronn
 */
public class ArrayFactory {

	/**
	 * Создать быстрый новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newArray(final Class<?> type) {
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
	public static <E> Array<E> newArray(final Class<?> type, final int size) {
		return new FastArray<E>((Class<E>) type, size);
	}

	/**
	 * Создать уникальное множество на основе быстрого массива.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newArraySet(final Class<?> type) {
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
	public static <E> Array<E> newArraySet(final Class<?> type, final int size) {
		return new FastArraySet<E>((Class<E>) type, size);
	}

	/**
	 * Создать потокобезопасный новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newAtomicArray(final Class<?> type) {
		return new AtomicArray<E>((Class<E>) type);
	}

	/**
	 * Создать потокобезопасный новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newConcurrentArray(final Class<?> type) {
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
	public static <E> Array<E> newConcurrentArray(final Class<?> type, final int size) {
		return new ConcurrentArray<E>((Class<E>) type, size);
	}

	/**
	 * Создать уникальное множество на основе конкурентного массива.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Array<E> newConcurrentArraySet(final Class<?> type) {
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
	public static <E> Array<E> newConcurrentArraySet(final Class<?> type, final int size) {
		return new ConcurrentArraySet<E>((Class<E>) type, size);
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
	 * @return новый динамический массив примтивного инт.
	 */
	public static IntegerArray newIntegerArray() {
		return new FastIntegerArray();
	}

	/**
	 * @param size начальный размер массива.
	 * @return новый динамический массив примтивного инт.
	 */
	public static IntegerArray newIntegerArray(final int size) {
		return new FastIntegerArray(size);
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
	 * @return новый динамический массив примтивного инт.
	 */
	public static LongArray newLongArray() {
		return new FastLongArray();
	}

	/**
	 * @param size начальный размер массива.
	 * @return новый динамический массив примтивного инт.
	 */
	public static LongArray newLongArray(final int size) {
		return new FastLongArray(size);
	}

	/**
	 * Создать сортируемый новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Comparable<E>> Array<E> newSortedArray(final Class<?> type) {
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
	public static <E extends Comparable<E>> Array<E> newSortedArray(final Class<?> type, final int size) {
		return new SortedArray<E>((Class<E>) type, size);
	}

	/**
	 * Создать синхронизированный новый массив указанного типа.
	 * 
	 * @param type тип массива.
	 * @return новый массив.
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
