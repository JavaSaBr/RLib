package rlib.util.array;

import rlib.util.ArrayUtils;

/**
 * Интерфейс для реализации динамического массива примитивного long. Главное
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
	public default boolean contains(long element) {

		final long[] array = array();

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
	public default boolean containsAll(long[] array) {

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
	public default boolean containsAll(LongArray array) {

		final long[] elements = array.array();

		for(int i = 0, length = array.size(); i < length; i++) {
			if(!contains(elements[i])) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Удаляет элемент по индексу с установкой последнего элемента на месте его.
	 *
	 * @param element индекс удаляемого элемента.
	 * @return удален ли объект.
	 */
	public default boolean fastRemove(int element) {

		final int index = indexOf(element);

		if(index > -1) {
			fastRemove(index);
		}

		return index > -1;
	}

	/**
	 * Удаляет элемент с установкой последнего элемента на месте его.
	 *
	 * @param element удаляемый элемент.
	 * @return удален ли объект.
	 */
	public default boolean fastRemove(long element) {

		final int index = indexOf(element);

		if(index > -1) {
			fastRemove(index);
		}

		return index > -1;
	}

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
	public default int indexOf(long element) {

		final long[] array = array();

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
	public default int lastIndexOf(long element) {

		final long[] array = array();

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
	 * @param target массив с элементами.
	 * @return удалены ли все указанные объекты.
	 */
	public default boolean removeAll(LongArray target) {

		if(target.isEmpty()) {
			return true;
		}

		final long[] array = target.array();

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
	public default boolean retainAll(LongArray target) {

		final long[] array = array();

		for(int i = 0, length = size(); i < length; i++) {
			if(!target.contains(array[i])) {
				fastRemove(i--);
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
	public default boolean slowRemove(long element) {

		final int index = indexOf(element);

		if(index > -1) {
			slowRemove(index);
		}

		return index > -1;
	}

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
	public default long[] toArray(long[] newArray) {

		final long[] array = array();

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
