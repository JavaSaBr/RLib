package rlib.util.array;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import rlib.function.LongBiObjectConsumer;
import rlib.function.TripleConsumer;
import rlib.util.ArrayUtils;
import rlib.util.ObjectUtils;
import rlib.util.pools.Reusable;

/**
 * Интерфейс для реализации динамических массивов. Главное преймущество по сравнению с ArrayList,
 * возможность итерировать самым быстрым способом и без ущерба для GC: <p>
 * <pre>
 * for(? element : array.array()) {
 *
 * 	if(element == null)	{
 * 		break;
 *    }
 *
 * 	// handle element
 * }
 * </pre>
 * <p> Для создания использовать {@link ArrayFactory}.
 *
 * @author Ronn
 * @created 27.02.2012
 */
public interface Array<E> extends Iterable<E>, Serializable, Reusable {

    /**
     * Добавление элемента в этот {@link Array}.
     */
    public Array<E> add(E object);

    /**
     * Добавление набора элементов в этот {@link Array}.
     */
    public Array<E> addAll(Array<? extends E> array);

    /**
     * Добавление всех элементов массива в этот {@link Array}.
     */
    public Array<E> addAll(E[] array);

    /**
     * Добавление всех элементов коллекции в этот {@link Array}.
     */
    public Array<E> addAll(Collection<? extends E> collection);

    /**
     * Применить функцию замены всех элементов.
     *
     * @param function применяемая функция.
     */
    public default void apply(final Function<? super E, ? extends E> function) {

        final E[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            array[i] = function.apply(array[i]);
        }
    }

    /**
     * @return возвращает обернутый массив элементов.
     */
    public E[] array();

    /**
     * @return стрим для работы с массивом.
     */
    public default Stream<E> stream() {
        return Arrays.stream(array(), 0, size());
    }

    /**
     * Проверка и при необходимости подготовка для расширения до указанного размера.
     */
    public default void checkSize(final int size) {
        throw new RuntimeException("not supported.");
    }

    /**
     * Очистить {@link Array}.
     */
    public Array<E> clear();

    /**
     * Проверяет, содержит ли {@link Array} указанный объект.
     *
     * @param object проверяемый объект.
     * @return вернется <code>false</code> в случае если переданный объект содержится в {@link
     * Array} либо он <code>null</code>.
     */
    public default boolean contains(final Object object) {
        return object != null && ArrayUtils.indexOf(array(), object, Object::equals, 0, size()) != -1;
    }

    /**
     * Проверяет, содержатся ли все элементы с указанного {@link Array} в этом {@link Array}.
     *
     * @param array набор проверяемых элементов.
     * @return <code>false</code> в случае если {@link Array} <code>null</code> или пуст или не
     * всего его элементы есть в этом {@link Array}.
     */
    public default boolean containsAll(final Array<?> array) {

        if (array == null || array.isEmpty()) {
            return false;
        }

        for (final Object element : array.array()) {

            if (element == null) {
                break;
            }

            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Проверяет, содержатся ли все элементы с указанного массива в этом {@link Array}.
     *
     * @param array массив элементов.
     * @return вернет <code>false</code> в случае когда массив <code>null</code> либо пуст либо не
     * все его элементы есть в этом {@link Array}.
     */
    public default boolean containsAll(final Object[] array) {

        if (array == null || array.length < 1) {
            return false;
        }

        for (final Object element : array) {
            if (!contains(element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public default void free() {
        clear();
    }

    /**
     * Удаляет элемент по индексу с установкой последнего элемента на месте его.
     *
     * @param index индекс удаляемого элемента.
     * @return удаленный элемент либо <code>null</code>.
     */
    public E fastRemove(int index);

    /**
     * Удаляет указанный элемент с установкой последнего элемента в {@link Array} на месте
     * удаленного.
     *
     * @param object удаляемый объект.
     * @return <code>true</code> если такой объект был в {@link Array}.
     */
    public default boolean fastRemove(final Object object) {
        return fastRemove(indexOf(object)) != null;
    }

    /**
     * @return первый элемент в {@link Array} либо <code>null</code>, если он пуст.
     */
    public default E first() {

        if (isEmpty()) {
            return null;
        }

        return get(0);
    }

    @Override
    public default void forEach(final Consumer<? super E> consumer) {
        for (final E element : array()) {

            if (element == null) {
                break;
            }

            consumer.accept(element);
        }
    }

    /**
     * Итерирование массива с дополнительным аргументом.
     *
     * @param argument дополнительный аргумент.
     * @param consumer функция для обработки элементов.
     */
    public default <T> void forEach(final T argument, final BiConsumer<T, E> consumer) {
        for (final E element : array()) {

            if (element == null) {
                break;
            }

            consumer.accept(argument, element);
        }
    }

    /**
     * Итерирование массива с двумя дополнительными аргументомами.
     *
     * @param first    первый дополнительный аргумент.
     * @param second   второй дополнительный аргумент.
     * @param consumer функция для обработки элементов.
     */
    public default <F, S> void forEach(final F first, final S second, final TripleConsumer<F, S, E> consumer) {
        for (final E element : array()) {

            if (element == null) {
                break;
            }

            consumer.accept(first, second, element);
        }
    }

    /**
     * Итерирование массива с двумя дополнительными аргументомами.
     *
     * @param first    первый дополнительный аргумент.
     * @param second   второй дополнительный аргумент.
     * @param consumer функция для обработки элементов.
     */
    public default <F> void forEach(final long first, final F second, final LongBiObjectConsumer<F, E> consumer) {
        for (final E element : array()) {

            if (element == null) {
                break;
            }

            consumer.accept(first, second, element);
        }
    }

    /**
     * Извлекает элемент по указанному индексу.
     *
     * @param index индекс элемента в {@link Array}.
     * @return элемент по указанному индексу.
     */
    public E get(int index);

    /**
     * Поиск индекса расположения указанного объекта в этом {@link Array}.
     *
     * @param object искомый объект.
     * @return <code>-1</code> в случае если такой объект небыл найден или если передоваемый объект
     * <code>null</code>.
     */
    public default int indexOf(final Object object) {

        if (object == null) {
            return -1;
        }

        int index = 0;

        for (final E element : array()) {

            if (element == null) {
                break;
            }

            if (ObjectUtils.equals(object, element)) {
                return index;
            }

            index++;
        }

        return -1;
    }

    /**
     * @return является ли {@link Array} пустым.
     */
    public default boolean isEmpty() {
        return size() < 1;
    }

    @Override
    public ArrayIterator<E> iterator();

    /**
     * @return последний элемент в этом {@link Array} либо <code>null</code>, если он пуст.
     */
    public default E last() {

        final int size = size();

        if (size < 1) {
            return null;
        }

        return get(size - 1);
    }

    /**
     * Поиск последнего индекса расположения указанного объекта в этом {@link Array}.
     *
     * @param object искомый объект.
     * @return <code>-1</code> в случае если такой объект не содержится в этом {@link Array}.
     */
    public default int lastIndexOf(final Object object) {

        if (object == null) {
            return -1;
        }

        final E[] array = array();

        int last = -1;

        for (int i = 0, length = size(); i < length; i++) {

            final E element = array[i];

            if (element.equals(object)) {
                last = i;
            }
        }

        return last;
    }

    /**
     * Удаляет первый элемент из {@link Array}.
     *
     * @return первый элемент этого {@link Array} либо <code>null</code>.
     */
    public default E poll() {
        return slowRemove(0);
    }

    /**
     * Удаляет последний элеметн из {@link Array}.
     *
     * @return последний элемент этого {@link Array} либо <code>null</code> если он пуст.
     */
    public default E pop() {
        return fastRemove(size() - 1);
    }

    /**
     * Блокировка записи в {@link Array}.
     */
    public default void readLock() {
    }

    /**
     * Разблокировка записи в {@link Array}.
     */
    public default void readUnlock() {
    }

    /**
     * Удаляет из {@link Array} все элементы из указанного {@link Array}.
     *
     * @param target {@link Array} с удаляемыми элементами.
     * @return <code>true</code> если переданный {@link Array} был не пуст и все в нем элементы
     * находились в этом {@link Array}.
     */
    public default boolean removeAll(final Array<?> target) {

        if (target == null || target.isEmpty()) {
            return false;
        }

        int count = 0;

        for (final Object element : target.array()) {

            if (element == null) {
                break;
            }

            if (fastRemove(element)) {
                count++;
            }
        }

        return count == target.size();
    }

    /**
     * Удаляет все элементы {@link Array}, которые отсутствуют в указанном {@link Array}.
     *
     * @param target {@link Array} с элементами.
     * @return удалены ли все объекты.
     */
    public default boolean retainAll(final Array<?> target) {

        final E[] array = array();

        for (int i = 0, length = size(); i < length; i++) {
            if (!target.contains(array[i])) {
                fastRemove(i--);
                length--;
            }
        }

        return true;
    }

    /**
     * Ищет первый элемент удовлетворяющий условию.
     *
     * @param predicate условия отбора элемента.
     * @return искомый объект либо null.
     */
    public default E search(final Predicate<E> predicate) {

        for (final E element : array()) {

            if (element == null) {
                break;
            }

            if (predicate.test(element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Ищет первый элемент удовлетворяющий условию c дополнительным аргументом.
     *
     * @param argument  дополнительный аргумент.
     * @param predicate условия отбора элемента.
     * @return искомый объект либо null.
     */
    public default <T> E search(final T argument, final BiPredicate<T, E> predicate) {

        for (final E element : array()) {

            if (element == null) {
                break;
            }

            if (predicate.test(argument, element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Установка указанного элемента по указанному индексу.
     *
     * @param index   индекс, по которому нужно устоновить элемент.
     * @param element элемент, который нужно добавит в массив.
     */
    public void set(int index, E element);

    /**
     * @return кол-во элементов в {@link Array}.
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
     * Сортировка {@link Array} компаратором.
     *
     * @param comparator компаратор для сортировки.
     */
    public default Array<E> sort(final Comparator<E> comparator) {
        ArrayUtils.sort(array(), comparator);
        return this;
    }

    /**
     * Копирует элементы {@link Array} в указаный массив, либо возвращает исходный в указанном
     * типе.
     *
     * @param newArray массив, в который нужно перенести.
     */
    @SuppressWarnings("unchecked")
    public default <T> T[] toArray(final T[] newArray) {

        final E[] array = array();

        if (newArray.length >= size()) {

            for (int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {

                if (array[i] == null) {
                    continue;
                }

                newArray[j++] = (T) array[i];
            }

            return newArray;
        }

        return (T[]) array;
    }

    /**
     * Уменьшает обернутый массив до текущего набора реальных элементов.
     */
    public Array<E> trimToSize();

    /**
     * Небезопасное добавления элемента без проверок.
     */
    public default Array<E> unsafeAdd(final E object) {
        return add(object);
    }

    /**
     * Блокировка чтения {@link Array}.
     */
    public default void writeLock() {
    }

    /**
     * Разблокировка чтения {@link Array}.
     */
    public default void writeUnlock() {
    }
}
