package rlib.util.array;

import rlib.util.ArrayUtils;

/**
 * Интерфейс для реализации динамического массива примитивного int. Главное
 * преймущество по сравнению с ArrayList, возможность работать с примитивами без
 * ущерба для GC:
 * 
 * <pre>
 * for(int i = 0, length = elements.size(); i &lt; length; i++) {
 * 
 * 	int value = elements.get(i);
 * 	// handle element
 * }
 * </pre>
 * 
 * Для создания использовать {@link ArrayFactory}.
 * 
 * 
 * @author Ronn
 */
public interface IntegerArray extends Iterable<Integer> {

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
	public default boolean contains(int element) {

		final int[] array = array();

		for(int i = 0, length = size(); i < length; i++) {
			if(array[i] == element) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Проверяет, содержатся ли все элементы с указанного массива в этом
	 * массиве.
	 *
	 * @param array массив элементов.
	 * @return содержит ли.
	 */
	public default boolean containsAll(int[] array) {

		for(int i = 0, length = array.length; i < length; i++) {
			if(!contains(array[i])) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Проверяет, содержатся ли все элементы с указанного массива в этом
	 * массиве.
	 *
	 * @param array массив элементов.
	 * @return содержит ли.
	 */
	public default boolean containsAll(IntegerArray array) {

		final int[] elements = array.array();

		for(int i = 0, length = array.size(); i < length; i++) {
			if(!contains(elements[i])) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Удаляет элемент с установкой последнего элемента на месте его.
	 *
	 * @param element удаляемый элемент.
	 * @return удален ли объект.
	 */
	public default boolean fastRemove(int element) {

		final int index = indexOf(element);

		if(index > -1) {
			fastRemoveByIndex(index);
		}

		return index > -1;
	}

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
	public default int indexOf(int element) {

		final int[] array = array();

		for(int i = 0, length = size(); i < length; i++) {
			if(element == array[i]) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * @return является ли массив пустым.
	 */
	public default boolean isEmpty() {
		return size() < 1;
	}

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
	public default int lastIndexOf(int element) {

		final int[] array = array();

		int last = -1;

		for(int i = 0, length = size(); i < length; i++) {
			if(element == array[i]) {
				last = i;
			}
		}

		return last;
	}

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
	 * @param target массив с элементами.
	 * @return удалены ли все указанные объекты.
	 */
	public default boolean removeAll(IntegerArray target) {

		if(target.isEmpty()) {
			return true;
		}

		final int[] array = target.array();

		for(int i = 0, length = target.size(); i < length; i++) {
			fastRemove(array[i]);
		}

		return true;
	}

	/**
	 * Удаляет все элементы массива, которые отсутствуют в указанном массиве.
	 *
	 * @param target массив с элементами.
	 * @return удалены ли все объекты.
	 */
	public default boolean retainAll(IntegerArray target) {

		final int[] array = array();

		for(int i = 0, length = size(); i < length; i++) {
			if(!target.contains(array[i])) {
				fastRemoveByIndex(i--);
				length--;
			}
		}

		return true;
	}

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
	public default boolean slowRemove(int element) {

		final int index = indexOf(element);

		if(index > -1) {
			slowRemoveByIndex(index);
		}

		return index > -1;
	}

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
	 * Копирует элементы коллекции в указаный массив, либо возвращает исходный в
	 * указанном типе.
	 *
	 * @param newArray массив, в который нужно перенести.
	 */
	public default int[] toArray(int[] newArray) {

		final int[] array = array();

		if(newArray.length >= size()) {

			for(int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {
				newArray[j++] = array[i];
			}

			return newArray;
		}

		return ArrayUtils.copyOf(array, 0);
	}

	/**
	 * Уменьшает массив до текущего набора реальных элементов.
	 *
	 * @return this.
	 */
	public IntegerArray trimToSize();

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
