package rlib.util.dictionary;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import rlib.function.ObjectIntConsumer;
import rlib.function.ObjectIntFunction;
import rlib.function.ObjectIntObjectConsumer;
import rlib.function.ObjectIntObjectFunction;
import rlib.function.ObjectLongObjectConsumer;

/**
 * Реализация набора утильных методов для работы со словарями.
 *
 * @author JavaSaBr
 */
public class DictionaryUtils {

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#writeLock()}.
     *
     * @param dictionary словарь.
     * @param consumer   функция для выполнения действий над словарем.
     */
    public static <V> void runInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final Consumer<ConcurrentIntegerDictionary<V>> consumer) {
        final long stamp = dictionary.writeLock();
        try {
            consumer.accept(dictionary);
        } finally {
            dictionary.writeUnlock(stamp);
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
        final long stamp = dictionary.writeLock();
        try {
            consumer.accept(dictionary, key);
        } finally {
            dictionary.writeUnlock(stamp);
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    public static <K, V> void runInWriteLock(final ConcurrentObjectDictionary<K, V> dictionary, final K key, final BiConsumer<ConcurrentObjectDictionary<K, V>, K> consumer) {
        final long stamp = dictionary.writeLock();
        try {
            consumer.accept(dictionary, key);
        } finally {
            dictionary.writeUnlock(stamp);
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
        final long stamp = dictionary.readLock();
        try {
            consumer.accept(dictionary, key);
        } finally {
            dictionary.readUnlock(stamp);
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
        final long stamp = dictionary.writeLock();
        try {
            return function.apply(dictionary, key);
        } finally {
            dictionary.writeUnlock(stamp);
        }
    }

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentObjectDictionary#readLock()}.
     *
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param function   функция для выполнения действий над словарем.
     * @return результат выполнения функции.
     */
    public static <K, V, R> R getInReadLock(final ConcurrentObjectDictionary<K, V> dictionary, final K key, final BiFunction<ConcurrentObjectDictionary<K, V>, K, R> function) {
        final long stamp = dictionary.readLock();
        try {
            return function.apply(dictionary, key);
        } finally {
            dictionary.readUnlock(stamp);
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
        final long stamp = dictionary.readLock();
        try {
            return function.apply(dictionary, key);
        } finally {
            dictionary.readUnlock(stamp);
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
        final long stamp = dictionary.writeLock();
        try {
            consumer.accept(dictionary, key, object);
        } finally {
            dictionary.writeUnlock(stamp);
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
        final long stamp = dictionary.writeLock();
        try {
            consumer.accept(dictionary, key, object);
        } finally {
            dictionary.writeUnlock(stamp);
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
        final long stamp = dictionary.readLock();
        try {
            consumer.accept(dictionary, key, object);
        } finally {
            dictionary.readUnlock(stamp);
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
        final long stamp = dictionary.writeLock();
        try {
            return function.apply(dictionary, key, object);
        } finally {
            dictionary.writeUnlock(stamp);
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
        final long stamp = dictionary.readLock();
        try {
            return function.apply(dictionary, key, object);
        } finally {
            dictionary.readUnlock(stamp);
        }
    }
}
