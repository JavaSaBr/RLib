package rlib.util.dictionary;

import java.util.function.Consumer;

import rlib.function.ObjectIntConsumer;
import rlib.function.ObjectIntFunction;
import rlib.function.ObjectIntObjectConsumer;
import rlib.function.ObjectIntObjectFunction;
import rlib.function.ObjectLongObjectConsumer;

/**
 * Реализация набора утильных методов для работы со словарями.
 *
 * @author Ronn
 */
public class DictionaryUtils {

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#writeLock()}.
     *
     * @param dictionary словарь.
     * @param consumer   функция для выполнения действий над словарем.
     */
    public static <V> void runInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final Consumer<ConcurrentIntegerDictionary<V>> consumer) {
        dictionary.writeLock();
        try {
            consumer.accept(dictionary);
        } finally {
            dictionary.writeUnlock();
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#writeLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    public static <V> void runInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final ObjectIntConsumer<ConcurrentIntegerDictionary<V>> consumer) {
        dictionary.writeLock();
        try {
            consumer.accept(dictionary, key);
        } finally {
            dictionary.writeUnlock();
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#readLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    public static <V> void runInReadLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final ObjectIntConsumer<ConcurrentIntegerDictionary<V>> consumer) {
        dictionary.readLock();
        try {
            consumer.accept(dictionary, key);
        } finally {
            dictionary.readUnlock();
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#writeLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param function   функция для выполнения действий над словарем.
     * @return результат выполнения функции.
     */
    public static <V, R> R getInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final ObjectIntFunction<ConcurrentIntegerDictionary<V>, R> function) {
        dictionary.writeLock();
        try {
            return function.apply(dictionary, key);
        } finally {
            dictionary.writeUnlock();
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#readLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param function   функция для выполнения действий над словарем.
     * @return результат выполнения функции.
     */
    public static <V, R> R getInReadLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final ObjectIntFunction<ConcurrentIntegerDictionary<V>, R> function) {
        dictionary.readLock();
        try {
            return function.apply(dictionary, key);
        } finally {
            dictionary.readUnlock();
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#writeLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param object     аргумент объект для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    public static <V> void runInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final V object, final ObjectIntObjectConsumer<ConcurrentIntegerDictionary<V>, V> consumer) {
        dictionary.writeLock();
        try {
            consumer.accept(dictionary, key, object);
        } finally {
            dictionary.writeUnlock();
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentLongDictionary#writeLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param object     аргумент объект для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    public static <V> void runInWriteLock(final ConcurrentLongDictionary<V> dictionary, final long key, final V object, final ObjectLongObjectConsumer<ConcurrentLongDictionary<V>, V> consumer) {
        dictionary.writeLock();
        try {
            consumer.accept(dictionary, key, object);
        } finally {
            dictionary.writeUnlock();
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#readLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param object     аргумент объект для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    public static <V> void runInReadLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final V object, final ObjectIntObjectConsumer<ConcurrentIntegerDictionary<V>, V> consumer) {
        dictionary.readLock();
        try {
            consumer.accept(dictionary, key, object);
        } finally {
            dictionary.readUnlock();
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#writeLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param object     аргумент объект для передачи в функцию.
     * @param function   функция для выполнения действий над словарем.
     * @return результат выполнения функции.
     */
    public static <V, R> R getInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final V object, final ObjectIntObjectFunction<ConcurrentIntegerDictionary<V>, V, R> function) {
        dictionary.writeLock();
        try {
            return function.apply(dictionary, key, object);
        } finally {
            dictionary.writeUnlock();
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#readLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param object     аргумент объект для передачи в функцию.
     * @param function   функция для выполнения действий над словарем.
     * @return результат выполнения функции.
     */
    public static <V, R> R getInReadLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final V object, final ObjectIntObjectFunction<ConcurrentIntegerDictionary<V>, V, R> function) {
        dictionary.readLock();
        try {
            return function.apply(dictionary, key, object);
        } finally {
            dictionary.readUnlock();
        }
    }
}
