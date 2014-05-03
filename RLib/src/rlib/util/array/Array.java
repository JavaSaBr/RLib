package rlib.util.array;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.util.ArrayUtils;
import rlib.util.ObjectUtils;
import rlib.util.pools.Foldable;

/**
 * Интерфейс для реализации динамических массивов.
 *
 * @author Ronn
 * @created 27.02.2012
 */
public interface Array<E> extends Iterable<E>, Serializable, Foldable {

	/**
	 * Добавление элемента в массив.
	 *
	 * @param object добавляемый элемент.
	 * @return ссылка на этот же массив.
	 */
	public Array<E> add(E object);

	/**
	 * Добавление набора элементов массива в этот массив.
	 *
	 * @param array добавляемый массив.
	 * @return ссылка на этот же массив.
	 */
	public Array<E> addAll(Array<? extends E> array);

	/**
	 * Добавление всех элементов массива в этот массив..
	 *
	 * @param array добавляемый массив.
	 * @return ссылка на этот же массив.
	 */
	public Array<E> addAll(E[] array);

	/**
	 * Применить функцию замены всех элементов.
	 *
	 * @param function применяемая функция.
	 */
	public default void apply(final Function<? super E, ? extends E> function) {

		final E[] array = array();

		for(int i = 0, length = size(); i < length; i++) {
			array[i] = function.apply(array[i]);
		}
	}

	/**
	 * @return возвращает массив элементов.
	 */
	public E[] array();

	/**
	 * Очистить массив путем зануления элементов.
	 *
	 * @return ссылка на этот же массив.
	 */
	public Array<E> clear();

	/**
	 * Проверяет, содержит ли массив указанный объект.
	 *
	 * @param object искомый объект.
	 * @return содержит ли.
	 */
	public default boolean contains(final Object object) {

		for(final E element : array()) {

			if(element == null) {
				break;
			}

			if(element.equals(object)) {
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
	public default boolean containsAll(final Array<?> array) {

		for(final Object element : array.array()) {

			if(element == null) {
				break;
			}

			if(!contains(element)) {
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
	public default boolean containsAll(final Object[] array) {

		for(final Object element : array) {
			if(!contains(element)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Удаляет элемент по индексу с установкой последнего элемента на месте его.
	 *
	 * @param index индекс удаляемого элемента.
	 * @return удаленный элемент.
	 */
	public E fastRemove(int index);

	/**
	 * Удаляет элемент с установкой последнего элемента на месте его.
	 *
	 * @param object удаляемый объект.
	 * @return удален ли объект.
	 */
	public default boolean fastRemove(final Object object) {
		return fastRemove(indexOf(object)) != null;
	}

	/**
	 * @return первый элемент в массиве.
	 */
	public default E first() {

		if(isEmpty()) {
			return null;
		}

		return get(0);
	}

	@Override
	public default void forEach(final Consumer<? super E> consumer) {

		for(final E element : array()) {

			if(element == null) {
				break;
			}

			consumer.accept(element);
		}
	}

	/**
	 * Извлекает элемент с указанным индексом.
	 *
	 * @param index индекс в массиве.
	 * @return элемент по указанному индексу.
	 */
	public E get(int index);

	/**
	 * Найти первый индекс указанного объекта.
	 *
	 * @param object искомый объект.
	 * @return первый индекс объекта.
	 */
	public default int indexOf(final Object object) {

		if(object == null) {
			return -1;
		}

		int index = 0;

		for(final E element : array()) {

			if(element == null) {
				break;
			}

			if(ObjectUtils.equals(object, element)) {
				return index;
			}

			index++;
		}

		return -1;
	}

	/**
	 * @return является ли массив пустым.
	 */
	public default boolean isEmpty() {
		return size() < 1;
	}

	/**
	 * @return итератор для перебора массива.
	 */
	@Override
	public ArrayIterator<E> iterator();

	/**
	 * @return последний элемент в массиве.
	 */
	public default E last() {

		final int size = size();

		if(size < 1) {
			return null;
		}

		return get(size - 1);
	}

	/**
	 * Найти последний индекс указанного объекта.
	 *
	 * @param object искомый объект.
	 * @return последний индекс искомого объекта.
	 */
	public default int lastIndexOf(final Object object) {

		if(object == null) {
			return -1;
		}

		final E[] array = array();

		int last = -1;

		for(int i = 0, length = size(); i < length; i++) {

			final E element = array[i];

			if(element.equals(object)) {
				last = i;
			}
		}

		return last;
	}

	/**
	 * @return первый элемент массива.
	 */
	public default E poll() {
		return slowRemove(0);
	}

	/**
	 * @return последний элемент массива.
	 */
	public default E pop() {
		return fastRemove(size() - 1);
	}

	/**
	 * Блокировка изменения массива на время чтения его.
	 */
	default public void readLock() {
	}

	/**
	 * Разблокировка изменения массива.
	 */
	default public void readUnlock() {
	}

	/**
	 * Удаляет из массива все элементы из указанного массива.
	 *
	 * @param array массив с элементами.
	 * @return удалены ли все указанные объекты.
	 */
	public default boolean removeAll(final Array<?> target) {

		if(target.isEmpty()) {
			return true;
		}

		for(final Object element : target.array()) {

			if(element == null) {
				break;
			}

			fastRemove(element);
		}

		return true;
	}

	/**
	 * Удаляет все элементы массива, которые отсутствуют в указанном массиве.
	 *
	 * @param array массив с элементами.
	 * @return удалены ли все объекты.
	 */
	public default boolean retainAll(final Array<?> target) {

		final E[] array = array();

		for(int i = 0, length = size(); i < length; i++) {
			if(!target.contains(array[i])) {
				fastRemove(i--);
				length--;
			}
		}

		return true;
	}

	/**
	 * Ищет подходящий элемент к указанному объекту.
	 *
	 * @param required сравниваемый объект.
	 * @param search поисковик подходящего объекта.
	 * @return искомый объект.
	 */
	public default E search(final E required, final Search<E> search) {

		final E[] array = array();

		for(int i = 0, length = size(); i < length; i++) {

			final E element = array[i];

			if(search.compare(required, element)) {
				return element;
			}
		}

		return null;
	}

	/**
	 * Установка указанного элемента по указанному индексу.
	 *
	 * @param index индекс, по которому нужно устоновить элемент.
	 * @param element элемент, который нужно добавит в массив.
	 */
	public void set(int index, E element);

	/**
	 * @return кол-во элементов в массиве.
	 */
	public int size();

	/**
	 * Удаляет элемент по индексу со сдвигом всех элементов после него.
	 *
	 * @param index индекс удаляемого элемента.
	 * @return удаленный элемент.
	 */
	public E slowRemove(int index);

	/**
	 * Удаляет элемент со сдвигом всех элементов после него.
	 *
	 * @param object удаляемый объект.
	 * @return удален ли объект.
	 */
	public boolean slowRemove(Object object);

	/**
	 * Сортировка колекции компаратором.
	 *
	 * @param comparator компаратор для сортировки.
	 * @return this.
	 */
	public default Array<E> sort(final Comparator<E> comparator) {
		ArrayUtils.sort(array(), comparator);
		return this;
	}

	/**
	 * Копирует элементы коллекции в указаный массив, либо возвращает исходный в
	 * указанном типе.
	 *
	 * @param newArray массив, в который нужно перенести.
	 */
	@SuppressWarnings("unchecked")
	public default <T> T[] toArray(final T[] newArray) {

		final E[] array = array();

		if(newArray.length >= size()) {

			for(int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {

				if(array[i] == null) {
					continue;
				}

				newArray[j++] = (T) array[i];
			}

			return newArray;
		}

		return (T[]) array;
	}

	/**
	 * Уменьшает массив до текущего набора реальных элементов.
	 *
	 * @return this.
	 */
	public Array<E> trimToSize();

	/**
	 * Блокировка чтений для изменения массива.
	 */
	default public void writeLock() {
	}

	/**
	 * Разблокировка чтения массива.
	 */
	default public void writeUnlock() {
	}
}
