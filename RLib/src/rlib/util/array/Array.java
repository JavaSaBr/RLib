package rlib.util.array;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.util.pools.Foldable;

/**
 * Интерфейс для реализации динамических массивов.
 *
 * @author Ronn
 * @created 27.02.2012
 */
public interface Array<E> extends Iterable<E>, Serializable, Foldable {

	/**
	 * Применить функцию на все элементы в массиве.
	 *
	 * @param consumer применяемая функция.
	 */
	public void accept(Consumer<? super E> consumer);

	/**
	 * Добавление элемента в массив.
	 *
	 * @param object добавляемый элемент.
	 * @return this.
	 */
	public Array<E> add(E object);

	/**
	 * Добавление всех элементов массива.
	 *
	 * @param array добавляемый массив.
	 * @return this.
	 */
	public Array<E> addAll(Array<? extends E> array);

	/**
	 * Добавление всех элементов массива.
	 *
	 * @param array добавляемый массив.
	 * @return this.
	 */
	public Array<E> addAll(E[] array);

	/**
	 * Применить функцию замены всех элементов.
	 *
	 * @param function применяемая функция.
	 */
	public void apply(Function<? super E, ? extends E> function);

	/**
	 * @return возвращает массив элементов.
	 */
	public E[] array();

	/**
	 * Очистить массив путем зануления элементов.
	 *
	 * @return this.
	 */
	public Array<E> clear();

	/**
	 * Проверяет, содержит ли массив указанный объект.
	 *
	 * @param object искомый объект.
	 * @return содержит ли.
	 */
	public boolean contains(Object object);

	/**
	 * Проверяет, содержатся ли все элементы с указанного массива в этом
	 * массиве.
	 *
	 * @param array массив элементов.
	 * @return содержит ли.
	 */
	public boolean containsAll(Array<?> array);

	/**
	 * Проверяет, содержатся ли все элементы с указанного массива в этом
	 * массиве.
	 *
	 * @param array массив элементов.
	 * @return содержит ли.
	 */
	public boolean containsAll(Object[] array);

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
	public boolean fastRemove(Object object);

	/**
	 * @return первый элемент в массиве.
	 */
	public E first();

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
	public int indexOf(Object object);

	/**
	 * @return является ли массив пустым.
	 */
	public boolean isEmpty();

	/**
	 * @return итератор для перебора массива.
	 */
	@Override
	public ArrayIterator<E> iterator();

	/**
	 * @return последний элемент в массиве.
	 */
	public E last();

	/**
	 * Найти последний индекс указанного объекта.
	 *
	 * @param object искомый объект.
	 * @return последний индекс искомого объекта.
	 */
	public int lastIndexOf(Object object);

	/**
	 * @return первый элемент массива.
	 */
	public E poll();

	/**
	 * @return последний элемент массива.
	 */
	public E pop();

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
	public boolean removeAll(Array<?> array);

	/**
	 * Удаляет все элементы массива, которые отсутствуют в указанном массиве.
	 *
	 * @param array массив с элементами.
	 * @return удалены ли все объекты.
	 */
	public boolean retainAll(Array<?> array);

	/**
	 * Ищет подходящий элемент к указанному объекту.
	 *
	 * @param required сравниваемый объект.
	 * @param search поисковик подходящего объекта.
	 * @return искомый объект.
	 */
	public E search(E required, Search<E> search);

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
	public Array<E> sort(Comparator<E> comparator);

	/**
	 * Копирует элементы коллекции в указаный массив, либо возвращает исходный в
	 * указанном типе.
	 *
	 * @param newArray массив, в который нужно перенести.
	 */
	public <T> T[] toArray(T[] newArray);

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
