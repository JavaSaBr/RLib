package rlib.util;

import java.util.Comparator;

import rlib.util.array.Array;
import rlib.util.array.IntegerArray;
import rlib.util.array.LongArray;

/**
 * Набор утильных методов для работы с массивами.
 *
 * @author Ronn
 * @created 07.04.2012
 */
public final class ArrayUtils {

    /**
     * Добавляет элемент в массив с расширением массива на +1.
     *
     * @param array   исходный массив.
     * @param element добавляемый элемент.
     * @param type    тип массива.
     * @return новый массив с указанным элементом в конце.
     */
    public static <T> T[] addToArray(T[] array, final T element, final Class<T> type) {

        if (array == null) {
            array = create(type, 1);
            array[0] = element;
            return array;
        }

        final int length = array.length;

        array = copyOf(array, 1);
        array[length] = element;

        return array;
    }

    /**
     * Зануление всех элементов массива.
     *
     * @param array массив, элементы которого нужно занулить.
     */
    public static void clear(final Object[] array) {
        for (int i = 0, length = array.length; i < length; i++) {
            array[i] = null;
        }
    }

    /**
     * Совместить 2 массива в один.
     *
     * @param base  исходный массив.
     * @param added добавочный массив.
     * @return новый общий массив.
     */
    public static int[] combine(final int[] base, final int[] added) {

        if (base == null) {
            return added;
        }

        if (added == null || added.length < 1) {
            return base;
        }

        final int[] result = new int[base.length + added.length];

        int index = 0;

        for (int i = 0, length = base.length; i < length; i++) {
            result[index++] = base[i];
        }

        for (int i = 0, length = added.length; i < length; i++) {
            result[index++] = added[i];
        }

        return result;
    }

    /**
     * Совмещение 2х масивов в 1.
     *
     * @param base  базовый массив.
     * @param added добавляемый массив.
     * @param type  тип массива.
     * @return новый массив.
     */
    public static <T, E extends T> T[] combine(final T[] base, final E[] added, final Class<T> type) {

        if (base == null) {
            return added;
        }

        if (added == null || added.length < 1) {
            return base;
        }

        final T[] result = create(type, base.length + added.length);

        int index = 0;

        for (int i = 0, length = base.length; i < length; i++) {
            result[index++] = base[i];
        }

        for (int i = 0, length = added.length; i < length; i++) {
            result[index++] = added[i];
        }

        return result;
    }

    /**
     * Проверка на содержания в массиве указанного значения.
     *
     * @param array проверяемый массив.
     * @param val   искомое значение.
     * @return содержит ли массив указанное значение.
     */
    public static boolean contains(final int[] array, final int val) {

        for (final int value : array) {
            if (value == val) {
                return true;
            }
        }

        return false;
    }

    /**
     * Проверка на содержания в массиве указанного значения.
     *
     * @param array  проверяемый массив.
     * @param object искомое значение.
     * @return содержит ли массив указанное значение.
     */
    public static boolean contains(final Object[] array, final Object object) {

        for (final Object element : array) {
            if (ObjectUtils.equals(element, object)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Копирование массива с созданием нового на указанный размер больше.
     *
     * @param old   исходный массив.
     * @param added сила расширения.
     * @return новый массив.
     */
    public static byte[] copyOf(final byte[] old, final int added) {

        final byte[] copy = new byte[old.length + added];

        System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));

        return copy;
    }

    /**
     * Копирование массива с созданием нового на указанный размер больше.
     *
     * @param old   исходный массив.
     * @param added сила расширения.
     * @return новый массив.
     */
    public static int[] copyOf(final int[] old, final int added) {

        final int[] copy = new int[old.length + added];

        System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));

        return copy;
    }

    /**
     * Копирование массива с созданием нового на указанный размер больше.
     *
     * @param old   исходный массив.
     * @param added сила расширения.
     * @return новый массив.
     */
    public static long[] copyOf(final long[] old, final int added) {

        final long[] copy = new long[old.length + added];

        System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));

        return copy;
    }

    /**
     * Копирование массива с созданием нового на указанный размер больше.
     *
     * @param old   исходный массив.
     * @param added сила расширения.
     * @return новый массив.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(final T[] old, final int added) {

        final Class<? extends Object[]> newType = old.getClass();

        final T[] copy = (T[]) create(newType.getComponentType(), old.length + added);

        System.arraycopy(old, 0, copy, 0, Math.min(old.length, copy.length));

        return copy;
    }

    /**
     * Копируем часть из массива и создаем новый массив из этой части.
     *
     * @param original исходный массив.
     * @param from     с какого индекса.
     * @param to       по какой индекс.
     * @return новый массив.
     */
    public static int[] copyOfRange(final int[] original, final int from, final int to) {

        final int newLength = to - from;

        final int[] copy = new int[newLength];

        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));

        return copy;
    }

    /**
     * Копируем часть из массива и создаем новый массив из этой части.
     *
     * @param original исходный массив.
     * @param from     с какого индекса.
     * @param to       по какой индекс.
     * @return новый массив.
     */
    public static long[] copyOfRange(final long[] original, final int from, final int to) {

        final int newLength = to - from;

        final long[] copy = new long[newLength];

        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));

        return copy;
    }

    /**
     * Копируем часть из массива и создаем новый массив из этой части.
     *
     * @param original исходный массив.
     * @param from     с какого индекса.
     * @param to       по какой индекс.
     * @return новый массив.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOfRange(final T[] original, final int from, final int to) {

        final Class<? extends Object[]> newType = original.getClass();

        final int newLength = to - from;

        final T[] copy = (T[]) create(newType.getComponentType(), newLength);

        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));

        return copy;
    }

    /**
     * Создание массива указанного типа.
     *
     * @param type тир массива.
     * @param size размер массива.
     * @return новый массив.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] create(final Class<?> type, final int size) {
        return (T[]) java.lang.reflect.Array.newInstance(type, size);
    }

    /**
     * Находит индекс объекта в указанном массиве.
     *
     * @param array  массив.
     * @param object искомый объект.
     * @return индекс оюъекта.
     */
    public static int indexOf(final Object[] array, final Object object) {

        int index = 0;

        for (final Object element : array) {

            if (ObjectUtils.equals(element, object)) {
                return index;
            }

            index++;
        }

        return -1;
    }

    /**
     * Сортировка массива, недопустимы нулевые значения.
     *
     * @param array сортируемый массив.
     */
    public static void sort(final Comparable<?>[] array) {
        java.util.Arrays.sort(array);
    }

    /**
     * Сортировка массива, недопустимы нулевые значения.
     *
     * @param array сортируемый массив.
     */
    public static void sort(final int[] array) {
        java.util.Arrays.sort(array);
    }

    /**
     * Сортировка массива, недопустимы нулевые значения.
     *
     * @param array сортируемый массив.
     */
    public static void sort(final int[] array, final int fromIndex, final int toIndex) {
        java.util.Arrays.sort(array, fromIndex, toIndex);
    }

    /**
     * Сортировка массива, недопустимы нулевые значения.
     *
     * @param array сортируемый массив.
     */
    public static void sort(final long[] array, final int fromIndex, final int toIndex) {
        java.util.Arrays.sort(array, fromIndex, toIndex);
    }

    /**
     * Сортировка массива компаратором.
     *
     * @param array      сортируемый массив.
     * @param comparator компаратор для массива.
     */
    public static <T> void sort(final T[] array, final Comparator<? super T> comparator) {
        java.util.Arrays.sort(array, comparator);
    }

    /**
     * Конфектирует массив объектов строку.
     *
     * @param array массив объектов.
     * @return строковый вариант.
     */
    public static String toString(final Array<?> array) {

        if (array == null) {
            return "[]";
        }

        final String className = array.array().getClass().getSimpleName();

        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.size() - 1; i <= length; i++) {

            builder.append(String.valueOf(array.get(i)));

            if (i == length) {
                break;
            }

            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * Конфектирует массив объектов строку.
     *
     * @param array массив объектов.
     * @return строковый вариант.
     */
    public static String toString(final IntegerArray array) {

        if (array == null) {
            return "[]";
        }

        final String className = array.array().getClass().getSimpleName();
        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.size() - 1; i <= length; i++) {

            builder.append(String.valueOf(array.get(i)));

            if (i == length) {
                break;
            }

            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * Конфектирует массив объектов строку.
     *
     * @param array массив объектов.
     * @return строковый вариант.
     */
    public static String toString(final LongArray array) {

        if (array == null) {
            return "[]";
        }

        final String className = array.array().getClass().getSimpleName();
        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.size() - 1; i <= length; i++) {

            builder.append(String.valueOf(array.get(i)));

            if (i == length) {
                break;
            }

            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * Конфектирует массив объектов строку.
     *
     * @param array массив объектов.
     * @return строковый вариант.
     */
    public static String toString(final Object[] array) {

        if (array == null) {
            return "[]";
        }

        final String className = array.getClass().getSimpleName();
        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.length - 1; i <= length; i++) {

            builder.append(String.valueOf(array[i]));

            if (i == length) {
                break;
            }

            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * Добавление элемента в массив в блоке {@link Array#writeLock()}.
     *
     * @param array  массив, в который надо добавить элемент.
     * @param object добавляемый элемент.
     */
    public static <T, V extends T> void addInWriteLockTo(final Array<T> array, final V object) {
        array.writeLock();
        try {
            array.add(object);
        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Добавление элемента в массив в блоке synchronized.
     *
     * @param array  массив, в который надо добавить элемент.
     * @param object добавляемый элемент.
     */
    public static <T, V extends T> void addInSynchronizeTo(final Array<T> array, final V object) {
        synchronized (array) {
            array.add(object);
        }
    }

    /**
     * Быстрое удаление элемента из массива в блоке {@link Array#writeLock()}.
     *
     * @param array  массив, в котором надо удалить элемент.
     * @param object удаляемый элемент.
     */
    public static <T, V extends T> void fastRemoveInWriteLockTo(final Array<T> array, final V object) {
        array.writeLock();
        try {
            array.fastRemove(object);
        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Быстрое удаление элемента из массива в блоке synchronized.
     *
     * @param array  массив, в котором надо удалить элемент.
     * @param object удаляемый элемент.
     */
    public static <T, V extends T> void fastRemoveInSynchronizeTo(final Array<T> array, final V object) {
        synchronized (array) {
            array.fastRemove(object);
        }
    }

    /**
     * Добавление элементов в массив в блоке {@link Array#writeLock()}.
     *
     * @param array   массив, в который надо добавить элемент.
     * @param objects добавляемые элементы.
     */
    public static <T, V extends T> void addInWriteLockTo(Array<T> array, V[] objects) {
        array.writeLock();
        try {
            array.addAll(objects);
        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Быстрое удаление элементов их массива в блоке {@link Array#writeLock()}.
     *
     * @param array   массив, в котором надо удалить элементы.
     * @param objects удаляемый элементы.
     */
    public static <T, V extends T> void fastRemoveInWriteLockTo(Array<T> array, V[] objects) {
        array.writeLock();
        try {

            for (V object : objects) {
                array.fastRemove(object);
            }

        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Копирование содержимого source в destination в блоке {@link Array#readLock()}.
     *
     * @param source      копируемый массив.
     * @param destination массив в который копируем данные.
     */
    public static <T> void copyInReadLock(Array<T> source, Array<T> destination) {

        source.readLock();
        try {
            destination.addAll(source);
        } finally {
            source.readUnlock();
        }
    }

    private ArrayUtils() {
        throw new RuntimeException();
    }
}
