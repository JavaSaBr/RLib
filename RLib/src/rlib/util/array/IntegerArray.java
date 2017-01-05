package rlib.util.array;

import rlib.util.ArrayUtils;

/**
 * Интерфейс для реализации динамического массива примитивного int. Главное преймущество по сравнению с ArrayList,
 * возможность работать с примитивами без ущерба для GC: <p>
 * <pre>
 * for(int i = 0, length = elements.size(); i &lt; length; i++) {
 *
 * 	int value = elements.get(i);
 * 	// handle element
 * }
 * </pre>
 * <p> Для создания использовать {@link ArrayFactory}.
 *
 * @author JavaSaBr
 */
public interface IntegerArray extends Iterable<Integer> {

    /**
     * Добавление элемента в массив.
     */
    IntegerArray add(int element);

    /**
     * Добавление всех элементов массива в этот массив.
     */
    IntegerArray addAll(int[] array);

    /**
     * Добавление всех элементов массива в этот массив.
     */
    IntegerArray addAll(IntegerArray array);

    /**
     * @return обернутый реализацией массив элементов.
     */
    int[] array();

    /**
     * Очистить массив.
     */
    IntegerArray clear();

    /**
     * Проверяет, содержит ли массив указанный элемент.
     *
     * @param element искомый элемент.
     * @return содержит ли.
     */
    default boolean contains(final int element) {

        final int[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (array[i] == element) return true;
        }

        return false;
    }

    /**
     * Проверяет, содержатся ли все элементы с указанного массива в этом массиве.
     *
     * @param array проверяемый массив элементов.
     * @return содержит ли все элементы проверяемого массива.
     */
    default boolean containsAll(final int[] array) {

        for (final int val : array) {
            if (!contains(val)) return false;
        }

        return true;
    }

    /**
     * Проверяет, содержатся ли все элементы с указанного массива в этом массиве.
     *
     * @param array проверяемый массив элементов.
     * @return содержит ли все элементы проверяемого массива.
     */
    default boolean containsAll(final IntegerArray array) {

        final int[] elements = array.array();

        for (int i = 0, length = array.size(); i < length; i++) {
            if (!contains(elements[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Удаляет элемент с установкой последнего элемента на месте его.
     *
     * @param element удаляемый элемент.
     * @return удален ли элемент.
     */
    default boolean fastRemove(final int element) {

        final int index = indexOf(element);
        if (index > -1) fastRemoveByIndex(index);

        return index > -1;
    }

    /**
     * Удаляет элемент по индексу с установкой последнего элемента на месте его.
     *
     * @param index индекс удаляемого элемента.
     * @return удален ли элемент.
     */
    boolean fastRemoveByIndex(int index);

    /**
     * @return первый элемент в массиве либо -1.
     */
    int first();

    /**
     * Извлекает элемент с указанным индексом.
     *
     * @param index индекс в массиве.
     * @return элемент по указанному индексу.
     */
    int get(int index);

    /**
     * Найти первый индекс указанного элемента в массиве.
     *
     * @param element искомый элемент.
     * @return первый индекс элемента в массиве.
     */
    default int indexOf(final int element) {

        final int[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) return i;
        }

        return -1;
    }

    /**
     * @return является ли массив пустым.
     */
    default boolean isEmpty() {
        return size() < 1;
    }

    @Override
    ArrayIterator<Integer> iterator();

    /**
     * @return последний элемент в массиве либо -1.
     */
    int last();

    /**
     * Найти последний индекс указанного элемента в массиве.
     *
     * @param element искомый элемент.
     * @return последний индекс искомого элемента в массиве.
     */
    default int lastIndexOf(final int element) {

        final int[] array = array();

        int last = -1;

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) last = i;
        }

        return last;
    }

    /**
     * @return первый элемент массива либо -1.
     */
    int poll();

    /**
     * @return последний элемент массива либо -1.
     */
    int pop();

    /**
     * Блокировка изменения массива на время чтения его.
     */
    default void readLock() {
    }

    /**
     * Разблокировка изменения массива.
     */
    default void readUnlock() {
    }

    /**
     * Удаляет из массива все элементы из указанного массива.
     *
     * @param target массив с элементами.
     * @return удалены ли все указанные элементы.
     */
    default boolean removeAll(final IntegerArray target) {
        if (target.isEmpty()) return true;

        final int[] array = target.array();

        for (int i = 0, length = target.size(); i < length; i++) {
            fastRemove(array[i]);
        }

        return true;
    }

    /**
     * Удаляет все элементы массива, которые отсутствуют в указанном массиве.
     *
     * @param target массив с элементами.
     * @return удалены ли все элементы.
     */
    default boolean retainAll(final IntegerArray target) {

        final int[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (!target.contains(array[i])) {
                fastRemoveByIndex(i--);
                length--;
            }
        }

        return true;
    }

    /**
     * @return кол-во элементов в массиве.
     */
    int size();

    /**
     * Удаляет элемент со сдвигом всех элементов после него.
     *
     * @param element удаляемый элемент.
     * @return удален ли элемент.
     */
    default boolean slowRemove(final int element) {

        final int index = indexOf(element);
        if (index > -1) slowRemoveByIndex(index);

        return index > -1;
    }

    /**
     * Удаляет элемент по индексу со сдвигом всех элементов после него.
     *
     * @param index индекс удаляемого элемента.
     * @return удален ли элемент.
     */
    boolean slowRemoveByIndex(int index);

    /**
     * Сортировка массива компаратором.
     */
    IntegerArray sort();

    /**
     * Копирует элементы коллекции в указаный массив, либо возвращает исходный в указанном типе.
     *
     * @param newArray массив, в который нужно перенести.
     */
    default int[] toArray(final int[] newArray) {

        final int[] array = array();

        if (newArray.length >= size()) {

            for (int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {
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
    IntegerArray trimToSize();

    /**
     * Блокировка чтений для изменения массива.
     */
    default void writeLock() {
    }

    /**
     * Разблокировка чтения массива.
     */
    default void writeUnlock() {
    }
}
