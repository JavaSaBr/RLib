package rlib.util.array;

/**
 * Интерфейс для реализации динамического массива примитивного long.
 * 
 * @author Ronn
 */
public interface LongArray extends Iterable<Long> {

	/**
	 * Добавление элемента в массив.
	 *
	 * @param element добавляемый элемент.
	 * @return this.
	 */
	public LongArray add(long element);

	/**
	 * Добавление всех элементов массива.
	 *
	 * @param array добавляемый массив.
	 * @return this.
	 */
	public LongArray addAll(long[] array);

	/**
	 * Добавление всех элементов массива.
	 *
	 * @param array добавляемый массив.
	 * @return this.
	 */
	public LongArray addAll(LongArray array);

	/**
	 * @return возвращает массив элементов.
	 */
	public long[] array();

	/**
	 * Очистить массив путем зануления элементов.
	 *
	 * @return this.
	 */
	public LongArray clear();

	/**
	 * Проверяет, содержит ли массив указанный элемент.
	 *
	 * @param element искомый элемент.
	 * @return содержит ли.
	 */
	public boolean contains(long element);

	/**
	 * Проверяет, содержатся ли все элементы с указанного массива в этом
	 * массиве.
	 *
	 * @param array массив элементов.
	 * @return содержит ли.
	 */
	public boolean containsAll(long[] array);

	/**
	 * Проверяет, содержатся ли все элементы с указанного массива в этом
	 * массиве.
	 *
	 * @param array массив элементов.
	 * @return содержит ли.
	 */
	public boolean containsAll(LongArray array);

	/**
	 * Удаляет элемент по индексу с установкой последнего элемента на месте его.
	 *
	 * @param index индекс удаляемого элемента.
	 * @return удален ли объект.
	 */
	public boolean fastRemove(int index);

	/**
	 * Удаляет элемент с установкой последнего элемента на месте его.
	 *
	 * @param element удаляемый элемент.
	 * @return удален ли объект.
	 */
	public boolean fastRemove(long element);

	/**
	 * @return первый элемент в массиве.
	 */
	public long first();

	/**
	 * Извлекает элемент с указанным индексом.
	 *
	 * @param index индекс в массиве.
	 * @return элемент по указанному индексу.
	 */
	public long get(int index);

	/**
	 * Найти первый индекс указанного объекта.
	 *
	 * @param element искомый элемент.
	 * @return первый индекс объекта.
	 */
	public int indexOf(long element);

	/**
	 * @return является ли массив пустым.
	 */
	public boolean isEmpty();

	@Override
	public ArrayIterator<Long> iterator();

	/**
	 * @return последний элемент в массиве.
	 */
	public long last();

	/**
	 * Найти последний индекс указанного элемента.
	 *
	 * @param element искомый элемент.
	 * @return последний индекс искомого элемента.
	 */
	public int lastIndexOf(long element);

	/**
	 * @return первый элемент массива.
	 */
	public long poll();

	/**
	 * @return последний элемент массива.
	 */
	public long pop();

	/**
	 * Блокировка изменения массива на время чтения его.
	 */
	public default void readLock() {
	}

	/**
	 * Разблокировка изменения массива.
	 */
	public default void readUnlock() {
	}

	/**
	 * Удаляет из массива все элементы из указанного массива.
	 *
	 * @param array массив с элементами.
	 * @return удалены ли все указанные объекты.
	 */
	public boolean removeAll(LongArray array);

	/**
	 * Удаляет все элементы массива, которые отсутствуют в указанном массиве.
	 *
	 * @param array массив с элементами.
	 * @return удалены ли все объекты.
	 */
	public boolean retainAll(LongArray array);

	/**
	 * @return кол-во элементов в массиве.
	 */
	public int size();

	/**
	 * Удаляет элемент по индексу со сдвигом всех элементов после него.
	 *
	 * @param index индекс удаляемого элемента.
	 * @return удален ли элемент.
	 */
	public boolean slowRemove(int index);

	/**
	 * Удаляет элемент со сдвигом всех элементов после него.
	 *
	 * @param element удаляемый элемент.
	 * @return удален ли элемент.
	 */
	public boolean slowRemove(long element);

	/**
	 * Сортировка колекции компаратором.
	 *
	 * @param comparator компаратор для сортировки.
	 * @return this.
	 */
	public LongArray sort();

	/**
	 * Копирует элементы коллекции в указаный массив, либо возвращает исходный в
	 * указанном типе.
	 *
	 * @param newArray массив, в который нужно перенести.
	 */
	public long[] toArray(long[] newArray);

	/**
	 * Уменьшает массив до текущего набора реальных элементов.
	 *
	 * @return this.
	 */
	public LongArray trimToSize();

	/**
	 * Блокировка чтений для изменения массива.
	 */
	public default void writeLock() {
	}

	/**
	 * Разблокировка чтения массива.
	 */
	public default void writeUnlock() {
	}
}
