package rlib.util;

import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import rlib.function.FunctionInt;
import rlib.function.ObjectIntFunction;
import rlib.function.ObjectIntPredicate;
import rlib.function.ObjectLongFunction;
import rlib.function.ObjectLongPredicate;
import rlib.function.TripleConsumer;
import rlib.function.TripleFunction;
import rlib.function.TriplePredicate;
import rlib.util.array.Array;
import rlib.util.array.IntegerArray;
import rlib.util.array.LongArray;

import static rlib.util.ClassUtils.unsafeCast;

/**
 * Набор утильных методов для работы с массивами.
 *
 * @author JavaSaBr
 * @created 07.04.2012
 */
public final class ArrayUtils {

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final Enum[] EMPTY_ENUM_ARRAY = new Enum[0];

    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];

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
        } else if (added == null || added.length < 1) {
            return base;
        }

        final int[] result = new int[base.length + added.length];

        int index = 0;

        for (int val : base) {
            result[index++] = val;
        }

        for (int val : added) {
            result[index++] = val;
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
        } else if (added == null || added.length < 1) {
            return base;
        }

        final T[] result = create(type, base.length + added.length);

        int index = 0;

        for (T object : base) {
            result[index++] = object;
        }

        for (E object : added) {
            result[index++] = object;
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
            if (value == val) return true;
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
    public static <T> T[] copyOfRange(final T[] original, final int from, final int to) {

        final Class<? extends Object[]> newType = original.getClass();
        final int newLength = to - from;

        final T[] copy = unsafeCast(create(newType.getComponentType(), newLength));

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
    public static <T> T[] create(final Class<?> type, final int size) {
        return unsafeCast(java.lang.reflect.Array.newInstance(type, size));
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
            if (ObjectUtils.equals(element, object)) return index;
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
        if (array == null) return "[]";

        final String className = array.array().getClass().getSimpleName();
        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.size() - 1; i <= length; i++) {
            builder.append(String.valueOf(array.get(i)));
            if (i == length) break;
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
        if (array == null) return "[]";

        final String className = array.array().getClass().getSimpleName();
        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.size() - 1; i <= length; i++) {
            builder.append(String.valueOf(array.get(i)));
            if (i == length) break;
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
        if (array == null) return "[]";

        final String className = array.array().getClass().getSimpleName();
        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.size() - 1; i <= length; i++) {
            builder.append(String.valueOf(array.get(i)));
            if (i == length) break;
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
        if (array == null) return "[]";

        final String className = array.getClass().getSimpleName();
        final StringBuilder builder = new StringBuilder(className.substring(0, className.length() - 1));

        for (int i = 0, length = array.length - 1; i <= length; i++) {
            builder.append(String.valueOf(array[i]));
            if (i == length) break;
            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#writeLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param function функция получения чего-то.
     * @return результат функции.
     */
    public static <T, R> R getInWriteLock(final Array<T> array, final Function<Array<T>, R> function) {
        if (array.isEmpty()) return null;
        array.writeLock();
        try {
            return function.apply(array);
        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#writeLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param consumer функция действия.
     */
    public static <T> void runInWriteLock(final Array<T> array, final Consumer<Array<T>> consumer) {
        array.writeLock();
        try {
            consumer.accept(array);
        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#readLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param consumer функция действия.
     */
    public static <T> void runInReadLock(final Array<T> array, final Consumer<Array<T>> consumer) {
        array.readLock();
        try {
            consumer.accept(array);
        } finally {
            array.readUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#readLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param function функция действия.
     * @return результат действия функции.
     */
    public static <T, R> R getInReadLock(final Array<T> array, final Function<Array<T>, R> function) {
        if (array.isEmpty()) return null;
        array.readLock();
        try {
            return function.apply(array);
        } finally {
            array.readUnlock();
        }
    }

    /**
     * Выполнение суммирование массива в блоке {@link Array#readLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param function функция получения сумы.
     * @return итоговая сумма.
     */
    public static <T> int sumInReadLock(final Array<T> array, final FunctionInt<T> function) {
        if (array.isEmpty()) return 0;
        array.readLock();
        try {

            int sum = 0;

            for (T element : array) {
                if (element == null) break;
                sum += function.apply(element);
            }

            return sum;

        } finally {
            array.readUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#writeLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param argument дополнительный аргумент.
     * @param function функция действия.
     * @return результат действия функции.
     */
    public static <T, V, R> R getInWriteLock(final Array<T> array, final V argument, final BiFunction<Array<T>, V, R> function) {
        if (array.isEmpty()) return null;
        array.writeLock();
        try {
            return function.apply(array, argument);
        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#writeLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param argument дополнительный аргумент.
     * @param consumer функция действия.
     */
    public static <T, V> void runInWriteLock(final Array<T> array, final V argument, final BiConsumer<Array<T>, V> consumer) {
        array.writeLock();
        try {
            consumer.accept(array, argument);
        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#readLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param argument дополнительный аргумент.
     * @param function функция действия.
     * @return результат действия функции.
     */
    public static <T, V, R> R getInReadLock(final Array<T> array, final V argument, final BiFunction<Array<T>, V, R> function) {
        if (array.isEmpty()) return null;
        array.readLock();
        try {
            return function.apply(array, argument);
        } finally {
            array.readUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#readLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param argument дополнительный аргумент.
     * @param function функция действия.
     * @return результат действия функции.
     */
    public static <T, V, R> R getInReadLock(final Array<T> array, final int argument, final ObjectIntFunction<Array<T>, R> function) {
        if (array.isEmpty()) return null;
        array.readLock();
        try {
            return function.apply(array, argument);
        } finally {
            array.readUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#readLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param argument дополнительный аргумент.
     * @param function функция действия.
     * @return результат действия функции.
     */
    public static <T, V, R> R getInReadLockL(final Array<T> array, final long argument, final ObjectLongFunction<Array<T>, R> function) {
        if (array.isEmpty()) return null;
        array.readLock();
        try {
            return function.apply(array, argument);
        } finally {
            array.readUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#readLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param argument дополнительный аргумент.
     * @param consumer функция действия.
     */
    public static <T, V> void runInReadLock(final Array<T> array, final V argument, final BiConsumer<Array<T>, V> consumer) {
        array.readLock();
        try {
            consumer.accept(array, argument);
        } finally {
            array.readUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#writeLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param first    первый дополнительный аргумент.
     * @param second   второй дополнительный аргумент.
     * @param consumer функция действия.
     */
    public static <T, F, S> void runInWriteLock(final Array<T> array, final F first, S second, final TripleConsumer<Array<T>, F, S> consumer) {
        array.writeLock();
        try {
            consumer.accept(array, first, second);
        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#writeLock()}.
     *
     * @param array     массив с которым надо работать.
     * @param first     первый дополнительный аргумент.
     * @param second    второй дополнительный аргумент.
     * @param predicate условие выполнения.
     * @param consumer  функция действия.
     */
    public static <T, F, S> void runInWriteLock(final Array<T> array, final F first, S second, final TriplePredicate<Array<T>, F, S> predicate, final TripleConsumer<Array<T>, F, S> consumer) {
        array.writeLock();
        try {
            if (predicate.test(array, first, second)) consumer.accept(array, first, second);
        } finally {
            array.writeUnlock();
        }
    }

    /**
     * Выполнение какого-то действия над массивом в блоке {@link Array#readLock()}.
     *
     * @param array    массив с которым надо работать.
     * @param first    первый дополнительный аргумент.
     * @param second   второй дополнительный аргумент.
     * @param consumer функция действия.
     */
    public static <T, F, S> void runInReadLock(final Array<T> array, final F first, S second, final TripleConsumer<Array<T>, F, S> consumer) {
        array.readLock();
        try {
            consumer.accept(array, first, second);
        } finally {
            array.readUnlock();
        }
    }

    /**
     * Перенос всех данных из одного массива в другой.
     *
     * @param source      массив откуда надо перенести данные.
     * @param destination массив в который надо перенести данные.
     * @param clearSource нужно ли после этого очищасть исходный массив.
     */
    public static <F extends S, S> void move(final Array<F> source, final Array<S> destination, final boolean clearSource) {
        if (source.isEmpty()) return;
        destination.addAll(source);
        if (clearSource) source.clear();
    }

    /**
     * Перенос всех данных из одного массива в другой с очисткой после этого первого массива.
     *
     * @param source      массив откуда надо перенести данные.
     * @param destination массив в который надо перенести данные.
     */
    public static <F extends S, S> void move(final Array<F> source, final Array<S> destination) {
        move(source, destination, true);
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
     * Обработка элемента массива.
     *
     * @param array    массив для обработки элементов.
     * @param consumer обработчик элемента.
     */
    public static <T> void forEach(final T[] array, final Consumer<T> consumer) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            consumer.accept(element);
        }
    }

    /**
     * Обработка элемента массива.
     *
     * @param array     массив для обработки элементов.
     * @param condition условие для приминения функции на элемент.
     * @param consumer  обработчик элемента.
     */
    public static <T> void forEach(final T[] array, final Predicate<T> condition, final Consumer<T> consumer) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            if (condition.test(element)) consumer.accept(element);
        }
    }

    /**
     * Обработка элемента массива с дополнительным аргументом.
     *
     * @param array    массив для обработки элементов.
     * @param argument дополнительный аргумент.
     * @param consumer обработчик элемента.
     */
    public static <T, F> void forEach(final T[] array, final F argument, final BiConsumer<T, F> consumer) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            consumer.accept(element, argument);
        }
    }

    /**
     * Обработка элемента массива с дополнительным аргументом.
     *
     * @param array      массив для обработки элементов.
     * @param argument   дополнительный аргумент.
     * @param getElement функция получения под элемента.
     * @param consumer   обработчик элемента.
     */
    public static <T, F, R> void forEach(final T[] array, final F argument, final Function<T, R> getElement, final BiConsumer<R, F> consumer) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            final R subElement = getElement.apply(element);
            if (subElement != null) consumer.accept(subElement, argument);
        }
    }

    /**
     * Обработка элемента массива с дополнительным аргументом.
     *
     * @param array       массив для обработки элементов.
     * @param argument    дополнительный аргумент.
     * @param conditional условие обработки элемента.
     * @param consumer    обработчик элемента.
     */
    public static <T, F> void forEach(final T[] array, final F argument, final Predicate<T> conditional, final BiConsumer<T, F> consumer) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            if (conditional.test(element)) {
                consumer.accept(element, argument);
            }
        }
    }

    /**
     * Обработка под элемента массива с дополнительным аргументом.
     *
     * @param array      массив для обработки элементов.
     * @param argument   дополнительный аргумент.
     * @param firstCond  условие отбора элемента массива.
     * @param getElement функция извлечения под элемента массива.
     * @param consumer   обработчик под элемента.
     */
    public static <T, R, F> void forEach(final T[] array, final F argument, final Predicate<T> firstCond, final Function<T, R> getElement, final BiConsumer<R, F> consumer) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            if (!firstCond.test(element)) continue;
            final R subElement = getElement.apply(element);
            consumer.accept(subElement, argument);
        }
    }

    /**
     * Обработка элемента массива с дополнительными аргументами.
     *
     * @param array    массив для обработки элементов.
     * @param first    первый дополнительный аргумент.
     * @param second   второй дополнительный аргумент.
     * @param consumer обработчик под элемента.
     */
    public static <T, F, S> void forEach(final T[] array, final F first, final S second, final TripleConsumer<T, F, S> consumer) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            consumer.accept(element, first, second);
        }
    }

    /**
     * Обработка под элемента массива с дополнительными аргументами.
     *
     * @param array      массив для обработки элементов.
     * @param first      первый дополнительный аргумент.
     * @param second     второй дополнительный аргумент.
     * @param getElement функция извлечения под элемента массива.
     * @param consumer   обработчик под элемента.
     */
    public static <T, R, F, S> void forEach(final T[] array, final F first, final S second, final TripleFunction<T, F, S, R> getElement, final TripleConsumer<R, F, S> consumer) {
        if (array == null || array.length < 1) return;
        for (final T element : array) {
            final R subElement = getElement.apply(element, first, second);
            consumer.accept(subElement, first, second);
        }
    }

    /**
     * Поиск индекса элемента в массиве с дополнительным аргументом.
     *
     * @param array     массив для поиска элемента.
     * @param argument  дополнительный аргумент.
     * @param condition условие отбора элемента.
     * @return индекс первого элемента, удовлетворяющего условия либо -1.
     */
    public static <T, F> int indexOf(final T[] array, final F argument, final BiPredicate<T, F> condition) {
        return indexOf(array, argument, condition, 0, array.length);
    }

    /**
     * Поиск индекса элемента в массиве с дополнительным аргументом.
     *
     * @param array      массив для поиска элемента.
     * @param argument   дополнительный аргумент.
     * @param condition  условие отбора элемента.
     * @param startIndex индекс ячейки с которой начинать.
     * @param endIndex   последний индекс проверяемой ячейки.
     * @return индекс первого элемента, удовлетворяющего условия либо -1.
     */
    public static <T, F> int indexOf(final T[] array, final F argument, final BiPredicate<T, F> condition, final int startIndex, final int endIndex) {
        if (array == null || array.length < 1) return -1;
        for (int i = startIndex; i < endIndex; i++) {
            if (condition.test(array[i], argument)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Функция подсчета кол-ва элементов удовлетворяющие указанному условию.
     *
     * @param array     массив для подсчета элементов.
     * @param predicate условие учета элемента.
     * @return кол-во нужных элементов.
     */
    public static <T> int count(final T[] array, final Predicate<T> predicate) {
        if (array == null || array.length < 1) return 0;

        int count = 0;

        for (final T element : array) {
            if (predicate.test(element)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Поиск элемента в массиве.
     *
     * @param array     массив для поиска элемента.
     * @param condition условие отбора элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T> T find(final T[] array, final Predicate<T> condition) {
        if (array == null || array.length < 1) return null;

        for (final T element : array) {
            if (condition.test(element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Поиск элемента в массиве с дополнительным аргументом.
     *
     * @param array     массив для поиска элемента.
     * @param argument  дополнительный аргумент.
     * @param condition условие отбора элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T, F> T find(final T[] array, final F argument, final BiPredicate<T, F> condition) {
        if (array == null || array.length < 1) return null;
        for (final T element : array) {
            if (condition.test(element, argument)) return element;
        }
        return null;
    }

    /**
     * Поиск под-элемента в массиве с дополнительным аргументом.
     *
     * @param array      массив для поиска элемента.
     * @param argument   дополнительный аргумент.
     * @param getElement функция извлечения под элемента массива.
     * @param secondCond условие отбора под элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T, R, F> R find(final T[] array, final F argument, final Function<T, R> getElement, final BiPredicate<R, F> secondCond) {
        if (array == null || array.length < 1) return null;

        for (final T element : array) {
            final R subElement = getElement.apply(element);
            if (secondCond.test(subElement, argument)) return subElement;
        }

        return null;
    }

    /**
     * Поиск под-элемента в массиве с дополнительным аргументом.
     *
     * @param array      массив для поиска элемента.
     * @param argument   дополнительный аргумент.
     * @param condition  условие для выполнения функции получения под элемента.
     * @param getElement функция извлечения под элемента массива.
     * @param secondCond условие отбора под элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T, R, F> R find(final T[] array, final F argument, final Predicate<T> condition, final Function<T, R> getElement, final BiPredicate<R, F> secondCond) {
        if (array == null || array.length < 1) return null;

        for (final T element : array) {
            if (!condition.test(element)) continue;
            final R subElement = getElement.apply(element);
            if (secondCond.test(subElement, argument)) return subElement;
        }

        return null;
    }

    /**
     * Поиск элемента в массиве с дополнительным аргументом.
     *
     * @param array     массив для поиска элемента.
     * @param argument  дополнительный аргумент.
     * @param condition условие отбора элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T> T find(final T[] array, final int argument, final ObjectIntPredicate<T> condition) {
        if (array == null || array.length < 1) return null;

        for (final T element : array) {
            if (condition.test(element, argument)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Поиск под-элемента в массиве с дополнительным аргументом.
     *
     * @param array      массив для поиска элемента.
     * @param argument   дополнительный аргумент.
     * @param getElement функция извлечения под элемента массива.
     * @param condition  условие отбора под элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T, R> R find(final T[] array, final int argument, final Function<T, R> getElement, final ObjectIntPredicate<R> condition) {
        if (array == null || array.length < 1) return null;

        for (final T element : array) {
            final R subElement = getElement.apply(element);
            if (condition.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Поиск под-элемента в массиве с дополнительным аргументом.
     *
     * @param array      массив для поиска элемента.
     * @param argument   дополнительный аргумент.
     * @param firstCond  условие по элементу массива.
     * @param getElement функция извлечения под элемента массива.
     * @param secondCond условие отбора под элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T, R> R find(final T[] array, final int argument, Predicate<T> firstCond, final Function<T, R> getElement, final ObjectIntPredicate<R> secondCond) {
        if (array == null || array.length < 1) return null;

        for (final T element : array) {
            if (!firstCond.test(element)) continue;
            final R subElement = getElement.apply(element);
            if (secondCond.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Поиск под-элемента в массиве с дополнительным аргументом.
     *
     * @param array      массив для поиска элемента.
     * @param argument   дополнительный аргумент.
     * @param firstCond  условие по элементу массива.
     * @param getElement функция извлечения под элемента массива.
     * @param secondCond условие отбора под элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T, R> R findL(final T[] array, final long argument, final Predicate<T> firstCond, final Function<T, R> getElement, final ObjectLongPredicate<R> secondCond) {
        if (array == null || array.length < 1) return null;

        for (final T element : array) {
            if (!firstCond.test(element)) continue;
            final R subElement = getElement.apply(element);
            if (secondCond.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Поиск под-элемента в массиве с дополнительным аргументом.
     *
     * @param array     массив для поиска элемента.
     * @param argument  дополнительный аргумент.
     * @param condition условие отбора элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T> T findL(final T[] array, final long argument, final ObjectLongPredicate<T> condition) {
        if (array == null || array.length < 1) return null;
        for (final T element : array) {
            if (condition.test(element, argument)) return element;
        }
        return null;
    }

    /**
     * Поиск под-элемента в массиве с дополнительным аргументом.
     *
     * @param array      массив для поиска элемента.
     * @param argument   дополнительный аргумент.
     * @param getElement функция извлечения под элемента массива.
     * @param condition  условие отбора элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T, R> R findL(final T[] array, final long argument, final Function<T, R> getElement, final ObjectLongPredicate<R> condition) {
        if (array == null || array.length < 1) return null;

        for (final T element : array) {
            final R subElement = getElement.apply(element);
            if (condition.test(subElement, argument)) {
                return subElement;
            }
        }

        return null;
    }

    /**
     * Поиск элемента в массиве c двумя дополнительными аргументами.
     *
     * @param array     массив для поиска элемента.
     * @param first     первый дополнительный аргумент.
     * @param second    второй дополнительный аргумент.
     * @param condition условие отбора элемента.
     * @return первый подходящий элемент либо null.
     */
    public static <T, F, S> T find(final T[] array, final F first, final S second, final TriplePredicate<T, F, S> condition) {
        if (array == null || array.length < 1) return null;
        for (final T element : array) {
            if (condition.test(element, first, second)) {
                return element;
            }
        }
        return null;
    }

    private ArrayUtils() {
        throw new RuntimeException();
    }
}
