package com.ss.rlib.util.dictionary;

import com.ss.rlib.function.LongBiObjectConsumer;
import com.ss.rlib.function.LongObjectConsumer;
import com.ss.rlib.util.array.LongArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

/**
 * Интерфейс для реализации словаря с примитивным ключем long.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public interface LongDictionary<V> extends Dictionary<LongKey, V> {

    /**
     * Проверка наличия значения в словаре по указанному ключу.
     *
     * @param key проверяемый ключ.
     * @return the boolean
     */
    default boolean containsKey(final long key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Вовзращает значения по указанному ключу.
     *
     * @param key ключ.
     * @return the v
     */
    @Nullable
    default V get(final long key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Вовзращает значения по указанному ключу, в случае отсутствия объекта, создается новый и
     * ложится по этому же ключу.
     *
     * @param key     ключ.
     * @param factory фабрика.
     * @return the v
     */
    @Nullable
    default V get(final long key, @NotNull final Supplier<V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Вовзращает значения по указанному ключу, в случае отсутствия объекта, создается новый и
     * ложится по этому же ключу.
     *
     * @param key     ключ.
     * @param factory фабрика.
     * @return the v
     */
    @Nullable
    default V get(final long key, @NotNull final LongFunction<V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Вовзращает значения по указанному ключу, в случае отсутствия объекта, создается новый с
     * учетом дополнительного аргумента и ложится по этому же ключу.
     *
     * @param <T>      the type parameter
     * @param key      ключ.
     * @param argument дополнительный аргумент.
     * @param factory  фабрика.
     * @return the v
     */
    @Nullable
    default <T> V get(final long key, @Nullable final T argument, @NotNull final Function<T, V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Key long array long array.
     *
     * @return массив ключей словаря.
     */
    @NotNull
    default LongArray keyLongArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * Key long array long array.
     *
     * @param container контейнер для ключей.
     * @return массив ключей словаря.
     */
    @NotNull
    default LongArray keyLongArray(@NotNull final LongArray container) {
        throw new UnsupportedOperationException();
    }

    /**
     * Добавляет новое значение по указанному ключу, и если уже есть элемент с таким ключем,
     * возвращает его.
     *
     * @param key   ключ значения.
     * @param value вставляемое значение.
     * @return the v
     */
    @Nullable
    default V put(final long key, @Nullable final V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Удаляет значение по ключу.
     *
     * @param key ключ значения.
     * @return the v
     */
    @Nullable
    default V remove(final long key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Пробег по словарю с просмотром ключа и значения.
     *
     * @param consumer функция обработки ключа и значения.
     */
    default void forEach(@NotNull final LongObjectConsumer<V> consumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Пробег по словарю с просмотром ключа и значения и дополнительным аргументом.
     *
     * @param <T>      the type parameter
     * @param argument дополнительный аргумент.
     * @param consumer функция обработки ключа и значения.
     */
    default <T> void forEach(@Nullable final T argument, @NotNull final LongBiObjectConsumer<V, T> consumer) {
        throw new UnsupportedOperationException();
    }
}
