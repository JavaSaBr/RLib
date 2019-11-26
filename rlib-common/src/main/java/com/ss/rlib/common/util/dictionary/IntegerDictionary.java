package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.IntBiObjectConsumer;
import com.ss.rlib.common.function.IntObjectConsumer;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.IntegerArray;
import com.ss.rlib.common.util.array.MutableIntegerArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * The interface to implement a dictionary which uses int as key.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public interface IntegerDictionary<V> extends Dictionary<IntKey, V> {

    /**
     * Create a new integer dictionary for the value's type.
     *
     * @param valueType the value's type.
     * @param <V>       the value's type.
     * @return the new integer dictionary.
     */
    static <V> @NotNull IntegerDictionary<V> ofType(@NotNull Class<? super V> valueType) {
        return DictionaryFactory.newIntegerDictionary();
    }

    /**
     * Create a new integer dictionary for the values.
     *
     * @param values the key-value values.
     * @param <V>    the value's type.
     * @return the new integer dictionary.
     */
    static <V> @NotNull IntegerDictionary<V> of(@NotNull Object... values) {

        if (values.length < 2 || values.length % 2 != 0) {
            throw new IllegalArgumentException("Incorrect argument's count.");
        }

        IntegerDictionary<V> dictionary = DictionaryFactory.newIntegerDictionary();

        for (int i = 0, length = values.length - 2; i <= length; i += 2) {
            dictionary.put((Integer) values[i], (V) values[i + 1]);
        }

        return dictionary;
    }

    /**
     * Return true if this dictionary contains a mapping for the specified key.
     *
     * @param key key whose presence in this dictionary is to be tested.
     * @return true if this dictionary contains a mapping for the specified key.
     */
    default boolean containsKey(int key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the value to which the specified key is mapped, or {@code null} if this dictionary
     * contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value to which the specified key is mapped, or {@code null} if this dictionary contains no mapping for the key.
     */
    default @Nullable V get(final int key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     * @return the stored value by the key or the new value.
     */
    default @NotNull V getOrCompute(int key, @NotNull Supplier<@NotNull V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     * @return the stored value by the key or the new value.
     */
    default @NotNull V getOrCompute(int key, @NotNull IntFunction<@NotNull V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param <T>      the argument's type.
     * @param key      the key.
     * @param argument the additional argument.
     * @param factory  the factory.
     * @return the stored value by the key or the new value.
     * @see #getOrCompute(int, Object, Function)
     */

    default <T> @Nullable V get(int key, @Nullable T argument, @NotNull Function<T, V> factory) {
        return getOrCompute(key, argument, factory);
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param <T>      the argument's type.
     * @param key      the key.
     * @param argument the additional argument.
     * @param factory  the factory.
     * @return the stored value by the key or the new value.
     */
    default <T> @Nullable V getOrCompute(
        int key,
        @NotNull T argument,
        @NotNull Function<@NotNull T, @NotNull V> factory
    ) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an array with all keys of this dictionary.
     *
     * @return the array with all keys of this dictionary.
     */
    default @NotNull IntegerArray keyArray() {
        return keyArray(ArrayFactory.newMutableIntegerArray(size()));
    }

    /**
     * Put to the array all keys of this dictionary.
     *
     * @param container the container.
     * @return the container with all keys.
     */
    default @NotNull IntegerArray keyArray(@NotNull MutableIntegerArray container) {
        throw new UnsupportedOperationException();
    }

    /**
     * Put the value by the key.
     *
     * @param key   the value's key.
     * @param value the value.
     * @return the previous value for the key or null.
     */
    default @Nullable V put(int key, @NotNull V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a mapping of the key.
     *
     * @param key the key.
     * @return the previous value for the key or null.
     */
    default @Nullable V remove(int key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param consumer the consumer.
     */
    default void forEach(@NotNull IntObjectConsumer<@NotNull ? super V> consumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param argument the argument.
     * @param consumer the consumer.
     * @param <T>      the argument's type.
     */
    default <T> void forEach(
        @NotNull T argument,
        @NotNull IntBiObjectConsumer<@NotNull ? super V, @NotNull ? super T> consumer
    ) {
        throw new UnsupportedOperationException();
    }
}
