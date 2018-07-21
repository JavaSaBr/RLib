package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Реализация набора утильных методов для работы со словарями.
 *
 * @author JavaSaBr
 */
public class DictionaryUtils {

    /**
     * Выполнение функции на словаре в блоке {@link ConcurrentIntegerDictionary#writeLock()}.
     *
     * @param <V>        the type parameter
     * @param dictionary словарь.
     * @param consumer   функция для выполнения действий над словарем.
     */
    @Deprecated
    public static <V> void runInWriteLock(final ConcurrentIntegerDictionary<V> dictionary,
                                          final Consumer<ConcurrentIntegerDictionary<V>> consumer) {
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
     * @param <V>        the type parameter
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    @Deprecated
    public static <V> void runInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final int key,
                                          final ObjectIntConsumer<ConcurrentIntegerDictionary<V>> consumer) {
        final long stamp = dictionary.writeLock();
        try {
            consumer.accept(dictionary, key);
        } finally {
            dictionary.writeUnlock(stamp);
        }
    }

    /**
     * Execute the consumer inside the block {@link ConcurrentLongDictionary#writeLock()}.
     *
     * @param <V>        the type parameter
     * @param dictionary the dictionary.
     * @param key        the key value.
     * @param consumer   the consumer.
     */
    @Deprecated
    public static <V> void runInWriteLock(@NotNull final ConcurrentLongDictionary<V> dictionary, final long key,
                                          @NotNull final ObjectLongConsumer<ConcurrentLongDictionary<V>> consumer) {
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
     * @param <V>        the type parameter
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    @Deprecated
    public static <V> void runInReadLock(final ConcurrentIntegerDictionary<V> dictionary, final int key,
                                         final ObjectIntConsumer<ConcurrentIntegerDictionary<V>> consumer) {
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
     * @param <V>        the type parameter
     * @param <R>        the type parameter
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param function   функция для выполнения действий над словарем.
     * @return результат выполнения функции.
     */
    @Deprecated
    public static <V, R> R getInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final int key,
                                          final ObjectIntFunction<ConcurrentIntegerDictionary<V>, R> function) {
        final long stamp = dictionary.writeLock();
        try {
            return function.apply(dictionary, key);
        } finally {
            dictionary.writeUnlock(stamp);
        }
    }


    /**
     * Get the value using a function from a dictionary in the block {@link
     * ConcurrentObjectDictionary#readLock()}*.
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param <R>        the type parameter
     * @param dictionary the dictionary.
     * @param key        the key value.
     * @param function   the function.
     * @return the result of the function.
     */
    @Nullable
    @Deprecated
    public static <K, V, R> R getInReadLock(@NotNull final ConcurrentObjectDictionary<K, V> dictionary,
                                            @NotNull final K key,
                                            @NotNull final BiFunction<ConcurrentObjectDictionary<K, V>, K, R> function) {
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
     * @param <V>        the type parameter
     * @param <R>        the type parameter
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param function   функция для выполнения действий над словарем.
     * @return результат выполнения функции.
     */
    @Deprecated
    public static <V, R> R getInReadLock(final ConcurrentIntegerDictionary<V> dictionary, final int key,
                                         final ObjectIntFunction<ConcurrentIntegerDictionary<V>, R> function) {
        final long stamp = dictionary.readLock();
        try {
            return function.apply(dictionary, key);
        } finally {
            dictionary.readUnlock(stamp);
        }
    }

    /**
     * Call the function inside the block {@link ConcurrentLongDictionary#readLock()}.
     *
     * @param <V>        the type parameter
     * @param <R>        the type parameter
     * @param dictionary the dictionary.
     * @param key        the key argument.
     * @param function   the function.
     * @return the function's result.
     */
    @Deprecated
    public static <V, R> R getInReadLock(final ConcurrentLongDictionary<V> dictionary, final long key,
                                         final ObjectLongFunction<ConcurrentLongDictionary<V>, R> function) {
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
     * @param <V>        the type parameter
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param object     аргумент объект для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    @Deprecated
    public static <V> void runInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final int key,
                                          final V object,
                                          final ObjectIntObjectConsumer<ConcurrentIntegerDictionary<V>, V> consumer) {
        final long stamp = dictionary.writeLock();
        try {
            consumer.accept(dictionary, key, object);
        } finally {
            dictionary.writeUnlock(stamp);
        }
    }

    /**
     * Execute the function for a dictionary in the block {@link ConcurrentLongDictionary#writeLock()}.
     *
     * @param <V>        the type parameter
     * @param dictionary the dictionary.
     * @param key        the key value.
     * @param object     the value.
     * @param consumer   the function.
     */
    @Deprecated
    public static <V> void runInWriteLock(@NotNull final ConcurrentLongDictionary<V> dictionary, final long key,
                                          @Nullable final V object,
                                          @NotNull final ObjectLongObjectConsumer<ConcurrentLongDictionary<V>, V> consumer) {
        final long stamp = dictionary.writeLock();
        try {
            consumer.accept(dictionary, key, object);
        } finally {
            dictionary.writeUnlock(stamp);
        }
    }

    /**
     * Execute the function for a dictionary in the block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param <K>        the key's type.
     * @param <V>        the value's type.
     * @param dictionary the dictionary.
     * @param runnable   the function.
     */
    @Deprecated
    public static <K, V> void runInWriteLock(
            @NotNull ConcurrentObjectDictionary<K, V> dictionary,
            @NotNull Runnable runnable
    ) {
        long stamp = dictionary.writeLock();
        try {
            runnable.run();
        } finally {
            dictionary.writeUnlock(stamp);
        }
    }

    /**
     * Execute the function for a dictionary in the block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param <K>        the key's type.
     * @param <V>        the value's type.
     * @param dictionary the dictionary.
     * @param key        the key value.
     * @param consumer   the function.
     */
    @Deprecated
    public static <K, V> void runInWriteLock(
            @Nullable ConcurrentObjectDictionary<K, V> dictionary,
            @NotNull K key,
            @NotNull BiConsumer<ConcurrentObjectDictionary<K, V>, K> consumer
    ) {

        if (dictionary == null) {
            return;
        }

        long stamp = dictionary.writeLock();
        try {
            consumer.accept(dictionary, key);
        } finally {
            dictionary.writeUnlock(stamp);
        }
    }

    /**
     * Execute the function for a dictionary in the block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param dictionary the dictionary.
     * @param key        the key value.
     * @param object     the value.
     * @param consumer   the function.
     */
    @Deprecated
    public static <K, V> void runInWriteLock(@NotNull final ConcurrentObjectDictionary<K, V> dictionary,
                                             @NotNull final K key, @Nullable final V object,
                                             @NotNull final TripleConsumer<ConcurrentObjectDictionary<K, V>, K, V> consumer) {
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
     * @param <V>        the type parameter
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param object     аргумент объект для передачи в функцию.
     * @param consumer   функция для выполнения действий над словарем.
     */
    @Deprecated
    public static <V> void runInReadLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final V object,
                                         final ObjectIntObjectConsumer<ConcurrentIntegerDictionary<V>, V> consumer) {
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
     * @param <V>        the type parameter
     * @param <R>        the type parameter
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param object     аргумент объект для передачи в функцию.
     * @param function   функция для выполнения действий над словарем.
     * @return результат выполнения функции.
     */
    @Deprecated
    public static <V, R> R getInWriteLock(final ConcurrentIntegerDictionary<V> dictionary, final int key,
                                          final V object,
                                          final ObjectIntObjectFunction<ConcurrentIntegerDictionary<V>, V, R> function) {
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
     * @param <V>        the type parameter
     * @param <R>        the type parameter
     * @param dictionary словарь.
     * @param key        аргумент ключ для передачи в функцию.
     * @param object     аргумент объект для передачи в функцию.
     * @param function   функция для выполнения действий над словарем.
     * @return результат выполнения функции.
     */
    @Deprecated
    public static <V, R> R getInReadLock(final ConcurrentIntegerDictionary<V> dictionary, final int key, final V object,
                                         final ObjectIntObjectFunction<ConcurrentIntegerDictionary<V>, V, R> function) {
        final long stamp = dictionary.readLock();
        try {
            return function.apply(dictionary, key, object);
        } finally {
            dictionary.readUnlock(stamp);
        }
    }
}
