package rlib.util.array;

import rlib.util.ArrayUtils;

/**
 * Интерфейс для реализации динамического массива примитивного long. Главное преймущество по
 * сравнению с ArrayList, возможность работать с примитивами без ущерба для GC: <p>
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
public interface LongArray extends Iterable<Long> {

    /**
     * Добавление элемента в массив.
     */
    public LongArray add(long element);

    /**
     * Добавление всех элементов массива в этот массив.
     */
    public LongArray addAll(long[] array);

    /**
     * Добавление всех элементов массива в этот массив.
     */
    public LongArray addAll(LongArray array);

    /**
     * @return обернутый реализацией массив элементов.
     */
    public long[] array();

    /**
     * Очистить массив.
     */
    public LongArray clear();

    /**
     * Проверяет, содержит ли массив указанный элемент.
     *
     * @param element искомый элемент.
     * @return содержит ли.
     */
    public default boolean contains(final long element) {

        final long[] array = array();

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
    public default boolean containsAll(final long[] array) {

        for (final long val : array) {
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
    public default boolean containsAll(final LongArray array) {

        final long[] elements = array.array();

        for (int i = 0, length = array.size(); i < length; i++) {
            if (!contains(elements[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Удаляет элемент по индексу с установкой последнего элемента на месте его.
     *
     * @param index индекс удаляемого элемента.
     * @return удален ли элемент.
     */
    public boolean fastRemove(int index);

    /**
     * Удаляет элемент с установкой последнего элемента на месте его.
     *
     * @param element удаляемый элемент.
     * @return удален ли элемент.
     */
    public default boolean fastRemove(final long element) {

        final int index = indexOf(element);
        if (index > -1) fastRemove(index);

        return index > -1;
    }

    /**
     * @return первый элемент в массиве либо -1.
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
     * Найти первый индекс указанного элемента в массиве.
     *
     * @param element искомый элемент.
     * @return первый индекс элемента в массиве.
     */
    public default int indexOf(final long element) {

        final long[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) {
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
     * @return последний элемент в массиве либо -1.
     */
    public long last();

    /**
     * Найти последний индекс указанного элемента в массиве.
     *
     * @param element искомый элемент.
     * @return последний индекс искомого элемента в массиве.
     */
    public default int lastIndexOf(final long element) {

        final long[] array = array();

        int last = -1;

        for (int i = 0, length = size(); i < length; i++) {
            if (element == array[i]) {
                last = i;
            }
        }

        return last;
    }

    /**
     * @return первый элемент массива либо -1.
     */
    public long poll();

    /**
     * @return последний элемент массива либо -1.
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
     * @return удалены ли все указанные элементы.
     */
    public default boolean removeAll(final LongArray target) {
        if (target.isEmpty()) return true;

        final long[] array = target.array();

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
    public default boolean retainAll(final LongArray target) {

        final long[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (!target.contains(array[i])) {
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
    public default boolean slowRemove(final long element) {

        final int index = indexOf(element);
        if (index > -1) slowRemove(index);

        return index > -1;
    }

    /**
     * Сортировка массива компаратором.
     */
    public LongArray sort();

    /**
     * Копирует элементы коллекции в указаный массив, либо возвращает исходный в указанном типе.
     *
     * @param newArray массив, в который нужно перенести.
     */
    public default long[] toArray(final long[] newArray) {

        final long[] array = array();

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
