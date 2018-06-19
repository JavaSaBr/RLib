package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.IntBiObjectConsumer;
import com.ss.rlib.common.function.IntObjectConsumer;
import com.ss.rlib.common.util.array.IntegerArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * The interface Integer dictionary.
 *
 * @param <V> the type parameter
 */
public interface IntegerDictionary<V> extends Dictionary<IntKey, V> {

    /**
     * Create a new integer dictionary for the value's type.
     *
     * @param valueType the value's type.
     * @param <V>       the value's type.
     * @return the new object dictionary.
     */
    static <V> @NotNull IntegerDictionary<V> ofType(@NotNull Class<?> valueType) {
        return DictionaryFactory.newIntegerDictionary();
    }

    /**
     * Contains key boolean.
     *
     * @param key the key
     * @return the boolean
     */
    default boolean containsKey(final int key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get v.
     *
     * @param key the key
     * @return the v
     */
    @Nullable
    default V get(final int key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get v.
     *
     * @param key     the key
     * @param factory the factory
     * @return the v
     */
    @Nullable
    default V get(final int key, @NotNull final Supplier<V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get v.
     *
     * @param key     the key
     * @param factory the factory
     * @return the v
     */
    @Nullable
    default V get(final int key, @NotNull final IntFunction<V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get v.
     *
     * @param <T>      the type parameter
     * @param key      the key
     * @param argument the argument
     * @param factory  the factory
     * @return the v
     */
    @Nullable
    default <T> V get(final int key, @Nullable final T argument, @NotNull final Function<T, V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Key integer array integer array.
     *
     * @return the integer array
     */
    @NotNull
    default IntegerArray keyIntegerArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * Key integer array integer array.
     *
     * @param container the container
     * @return the integer array
     */
    @NotNull
    default IntegerArray keyIntegerArray(@NotNull final IntegerArray container) {
        throw new UnsupportedOperationException();
    }

    /**
     * Put v.
     *
     * @param key   the key
     * @param value the value
     * @return the v
     */
    @Nullable
    default V put(final int key, @Nullable final V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove v.
     *
     * @param key the key
     * @return the v
     */
    @Nullable
    default V remove(final int key) {
        throw new UnsupportedOperationException();
    }

    /**
     * For each.
     *
     * @param consumer the consumer
     */
    default void forEach(@NotNull final IntObjectConsumer<V> consumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * For each.
     *
     * @param <T>      the type parameter
     * @param argument the argument
     * @param consumer the consumer
     */
    default <T> void forEach(@Nullable final T argument, @NotNull final IntBiObjectConsumer<V, T> consumer) {
        throw new UnsupportedOperationException();
    }
}
