package rlib.util.array;

/**
 * Интерфейс для реализации динамического массива примитивного int.
 * 
 * @author Ronn
 */
public interface IntegerArray extends Iterable<Integer>
{
	/**
	 * Добавление элемента в массив.
	 *
	 * @param element добавляемый элемент.
	 * @return this.
	 */
	public IntegerArray add(int element);

	/**
	 * Добавление всех элементов массива.
	 *
	 * @param array добавляемый массив.
	 * @return this.
	 */
	public IntegerArray addAll(int[] array);

	/**
	 * Добавление всех элементов массива.
	 *
	 * @param array добавляемый массив.
	 * @return this.
	 */
	public IntegerArray addAll(IntegerArray array);

	/**
	 * @return возвращает массив элементов.
	 */
	public int[] array();

	/**
	 * Очистить массив путем зануления элементов.
	 *
	 * @return this.
	 */
	public IntegerArray clear();

	/**
	 * Проверяет, содержит ли массив указанный элемент.
	 *
     * @param element искомый элемент.
     * @return содержит ли.
     */
	public boolean contains(int element);

	/**
	 * Проверяет, содержатся ли все элементы с указанного массива в этом массиве.
	 *
	 * @param array массив элементов.
	 * @return содержит ли.
	 */
	public boolean containsAll(int[] array);

	/**
	 * Проверяет, содержатся ли все элементы с указанного массива в этом массиве.
	 *
	 * @param array массив элементов.
	 * @return содержит ли.
	 */
	public boolean containsAll(IntegerArray array);

	/**
	 * Удаляет элемент с установкой последнего элемента на месте его.
	 *
	 * @param element удаляемый элемент.
	 * @return удален ли объект.
	 */
	public boolean fastRemove(int element);

	/**
	 * Удаляет элемент по индексу с установкой последнего элемента на месте его.
	 *
	 * @param index индекс удаляемого элемента.
	 * @return удален ли объект.
	 */
	public boolean fastRemoveByIndex(int index);

	/**
	 * @return первый элемент в массиве.
	 */
	public int first();

	/**
	 * Извлекает элемент с указанным индексом.
	 *
	 * @param index индекс в массиве.
	 * @return элемент по указанному индексу.
	 */
	public int get(int index);

	/**
	 * Найти первый индекс указанного объекта.
	 *
	 * @param element искомый элемент.
	 * @return первый индекс объекта.
	 */
	public int indexOf(int element);

	/**
	 * @return является ли массив пустым.
	 */
	public boolean isEmpty();

	@Override
	public ArrayIterator<Integer> iterator();

	/**
	 * @return последний элемент в массиве.
	 */
	public int last();

	/**
	 * Найти последний индекс указанного элемента.
	 *
	 * @param element искомый элемент.
	 * @return последний индекс искомого элемента.
	 */
	public int lastIndexOf(int element);

	/**
	 * @return первый элемент массива.
	 */
	public int poll();

	/**
	 * @return последний элемент массива.
	 */
	public int pop();

	/**
	 * Блокировка изменения массива на время чтения его.
	 */
	public void readLock();

	/**
	 * Разблокировка изменения массива.
	 */
	public void readUnlock();

	/**
	 * Удаляет из массива все элементы из указанного массива.
	 *
	 * @param array массив с элементами.
	 * @return удалены ли все указанные объекты.
	 */
	public boolean removeAll(IntegerArray array);

    /**
     * Удаляет все элементы массива, которые отсутствуют в указанном массиве.
     *
     * @param array массив с элементами.
     * @return удалены ли все объекты.
     */
	public boolean retainAll(IntegerArray array);

	/**
	 * @return кол-во элементов в массиве.
	 */
	public int size();

	/**
	 * Удаляет элемент со сдвигом всех элементов после него.
	 *
	 * @param element удаляемый элемент.
	 * @return удален ли элемент.
	 */
	public boolean slowRemove(int element);

	/**
	 * Удаляет элемент по индексу со сдвигом всех элементов после него.
	 *
	 * @param index индекс удаляемого элемента.
	 * @return удален ли элемент.
	 */
	public boolean slowRemoveByIndex(int index);

	/**
	 * Сортировка колекции компаратором.
	 *
	 * @param comparator компаратор для сортировки.
	 * @return this.
	 */
	public IntegerArray sort();

	/**
	 * Копирует элементы коллекции в указаный массив, либо возвращает исходный в указанном типе.
	 *
	 * @param newArray массив, в который нужно перенести.
	 */
	public int[] toArray(int[] newArray);

	/**
	 * Уменьшает массив до текущего набора реальных элементов.
	 *
	 * @return this.
	 */
	public IntegerArray trimToSize();

	/**
	 * Блокировка чтений для изменения массива.
	 */
	public void writeLock();

	/**
	 * Разблокировка чтения массива.
	 */
	public void writeUnlock();
}
