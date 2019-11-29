package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.LongBiObjectConsumer;
import com.ss.rlib.common.function.LongObjectConsumer;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.array.LongArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

/**
 * The interface to implement a dictionary which uses long as key.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public interface LongDictionary<V> extends Dictionary<LongKey, V> {

    /**
     * Get an empty read-only long dictionary.
     *
     * @param <V> the value's type.
     * @return the read-only empty dictionary.
     */
    static <V> @NotNull LongDictionary<V> empty() {
        return ClassUtils.unsafeNNCast(DictionaryFactory.EMPTY_LD);
    }

    /**
     * Create a new long dictionary for the value's type.
     *
     * @param valueType the value's type.
     * @param <V>       the value's type.
     * @return the new long dictionary.
     */
    static <V> @NotNull LongDictionary<V> ofType(@NotNull Class<? super V> valueType) {
        return DictionaryFactory.newLongDictionary();
    }

    /**
     * Create a new long dictionary for the values.
     *
     * @param values the key-value values.
     * @param <V>    the value's type.
     * @return the new long dictionary.
     */
    static <V> @NotNull LongDictionary<V> of(@NotNull Object... values) {

        if (values.length < 2 || values.length % 2 != 0) {
            throw new IllegalArgumentException("Incorrect argument's count.");
        }

        var dictionary = DictionaryFactory.<V>newLongDictionary();

        for (int i = 0, length = values.length - 2; i <= length; i += 2) {
            dictionary.put(((Number) values[i]).longValue(), (V) values[i + 1]);
        }

        return dictionary;
    }

    static <V, M extends LongDictionary<V>> @NotNull M append(
        @NotNull M first,
        @NotNull M second
    ) {
        second.copyTo(first);
        return first;
    }

    /**
     * Return true if this dictionary contains a mapping for the specified key.
     *
     * @param key key whose presence in this dictionary is to be tested.
     * @return true if this dictionary contains a mapping for the specified key.
     */
    default boolean containsKey(long key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the value to which the specified key is mapped, or {@code null} if this dictionary
     * contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value to which the specified key is mapped, or {@code null} if this dictionary contains no mapping for the key.
     */
    default @Nullable V get(long key) {
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
    default @NotNull V getOrCompute(long key, @NotNull Supplier<@NotNull V> factory) {
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
    default @NotNull V getOrCompute(long key, @NotNull LongFunction<@NotNull V> factory) {
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
     */
    default <T> @Nullable V getOrCompute(
        long key,
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
    default @NotNull LongArray keyArray() {
        return keyArray(ArrayFactory.newLongArray(size()));
    }

    /**
     * Put to the array all keys of this dictionary.
     *
     * @param container the container.
     * @return the container with all keys.
     */
    default @NotNull LongArray keyArray(@NotNull LongArray container) {
        throw new UnsupportedOperationException();
    }

    /**
     * Put the value by the key.
     *
     * @param key   the value's key.
     * @param value the value.
     * @return the previous value for the key or null.
     */
    default @Nullable V put(long key, @NotNull V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a mapping of the key.
     *
     * @param key the key.
     * @return the previous value for the key or null.
     */
    default @Nullable V remove(long key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param consumer the consumer.
     */
    default void forEach(@NotNull LongObjectConsumer<@NotNull V> consumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param argument the argument.
     * @param consumer the consumer.
     * @param <T>      the argument's type.
     */
    default <T> void forEach(@NotNull T argument, @NotNull LongBiObjectConsumer<@NotNull V, @NotNull T> consumer) {
        throw new UnsupportedOperationException();
    }
}
