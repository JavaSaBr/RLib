package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.FourObjectConsumer;
import com.ss.rlib.common.function.TripleConsumer;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The interface for implementing a key-value dictionary which using an object key.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public interface ObjectDictionary<K, V> extends Dictionary<K, V> {

    /**
     * Create a new object dictionary for the key's type and value's type.
     *
     * @param keyValueType the key's and value's type.
     * @param <T>          the key's and value's type.
     * @return the new object dictionary.
     */
    static <T> @NotNull ObjectDictionary<T, T> ofType(@NotNull Class<? super T> keyValueType) {
        return DictionaryFactory.newObjectDictionary();
    }

    /**
     * Create a new object dictionary for the key's type and value's type.
     *
     * @param keyType   the key's type.
     * @param valueType the value's type.
     * @param <K>       the key's type.
     * @param <V>       the value's type.
     * @return the new object dictionary.
     */
    static <K, V> @NotNull ObjectDictionary<K, V> ofType(
            @NotNull Class<? super K> keyType,
            @NotNull Class<? super V> valueType
    ) {
        return DictionaryFactory.newObjectDictionary();
    }

    /**
     * Create a new object dictionary for the values.
     *
     * @param <K>       the key's type.
     * @param <V>       the value's type.
     * @return the new object dictionary.
     */
    static <K, V> @NotNull ObjectDictionary<K, V> of(@NotNull Object... values) {

        if (values.length < 2 || values.length % 2 != 0) {
            throw new IllegalArgumentException("Incorect argument's count.");
        }

        ObjectDictionary<K, V> dictionary = DictionaryFactory.newObjectDictionary();

        for (int i = 0, length = values.length - 2; i <= length; i += 2) {
            dictionary.put((K) values[i], (V) values[i + 1]);
        }

        return dictionary;
    }

    /**
     * Return <tt>true</tt> if this dictionary contains a mapping for the specified key.  More
     * formally, returns <tt>true</tt> if and only if this dictionary contains a mapping for a key
     * <tt>k</tt> such that <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be at most
     * one such mapping.)
     *
     * @param key key whose presence in this dictionary is to be tested.
     * @return <tt>true</tt> if this dictionary contains a mapping for the specified key.
     */
    default boolean containsKey(@NotNull K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the value to which the specified key is mapped, or {@code null} if this dictionary
     * contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value to which the specified key is mapped, or {@code null} if this dictionary contains no mapping for the key.
     */
    default @Nullable V get(@NotNull K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the optional value to which the specified key is mapped, or {@code null} if this dictionary
     * contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the optional value to which the specified key is mapped.
     */
    default @NotNull Optional<V> getOptional(@NotNull K key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     * @return the stored value by the key or the new value.
     * @see #getOrCompute(Object, Supplier)
     */
    @Deprecated
    default @NotNull V get(@NotNull K key, @NotNull Supplier<V> factory) {
        return getOrCompute(key, factory);
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     * @return the stored value by the key or the new value.
     */
    default @NotNull V getOrCompute(@NotNull K key, @NotNull Supplier<@NotNull V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     * @return the stored value by the key or the new value.
     * @see #getOrCompute(Object, Function)
     */
    @Deprecated
    default @NotNull V get(@NotNull K key, @NotNull Function<K, V> factory) {
        return getOrCompute(key, factory);
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     * @return the stored value by the key or the new value.
     */
    default @NotNull V getOrCompute(@NotNull K key, @NotNull Function<@NotNull K, @NotNull V> factory) {
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
     * @see #getOrCompute(Object, Object, Function)
     */
    @Deprecated
    default <T> @NotNull V get(@NotNull K key, @Nullable T argument, @NotNull Function<T, V> factory) {
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
    default <T> @NotNull V getOrCompute(
            @NotNull K key,
            @NotNull T argument,
            @NotNull Function<@NotNull T, @NotNull V> factory
    ) {
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
     * @see #getOrCompute(Object, Object, BiFunction)
     */
    default <T> @NotNull V get(@NotNull K key, @Nullable T argument, @NotNull BiFunction<K, T, V> factory) {
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
    default <T> @NotNull V getOrCompute(
            @NotNull K key,
            @NotNull T argument,
            @NotNull BiFunction<@NotNull K, @NotNull T, @NotNull V> factory
    ) {

        throw new UnsupportedOperationException();
    }

    /**
     * Put to the array all keys of this dictionary.
     *
     * @param container the container.
     * @return the array with all keys.
     */
    default @NotNull Array<K> keyArray(@NotNull Array<K> container) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an array with all keys of this dictionary.
     *
     * @param type the key's type.
     * @return the array with all keys of this dictionary.
     */
    default @NotNull Array<K> keyArray(@NotNull Class<K> type) {
        return keyArray(ArrayFactory.newArray(type, size()));
    }

    /**
     * Associates the specified value with the specified key in this dictionary (optional
     * operation).  If the dictionary previously contained a mapping for the key, the old value is
     * replaced by the specified value.  (A dictionary <tt>m</tt> is said to contain a mapping for a
     * key <tt>k</tt> if and only if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map previously associated <tt>null</tt> with <tt>key</tt>, if the implementation supports <tt>null</tt> values.)
     */
    default @Nullable V put(@NotNull K key, @Nullable V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Associates the specified value with the specified key in this dictionary (optional
     * operation).  If the dictionary previously contained a mapping for the key, the old value is
     * replaced by the specified value.  (A dictionary <tt>m</tt> is said to contain a mapping for a
     * key <tt>k</tt> if and only if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous optional value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for <tt>key</tt>.
     */
    default @NotNull Optional<V> putOptional(@NotNull K key, @Nullable V value) {
        return Optional.ofNullable(put(key, value));
    }

    /**
     * Removes the mapping for a key from this dictionary if it is present (optional operation).
     * More formally, if this dictionary contains a mapping from key <tt>k</tt> to value <tt>v</tt>
     * such that <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is removed.  (The
     * map can contain at most one such mapping.)
     * <p>Returns the value to which this dictionary previously associated the key, or <tt>null</tt>
     * if the dictionary contained no mapping for the key.
     * <p>If this dictionary permits null values, then a return value of <tt>null</tt> does not
     * <i>necessarily</i> indicate that the dictionary contained no mapping for the key; it's also
     * possible that the dictionary explicitly mapped the key to <tt>null</tt>.
     * <p>The dictionary will not contain a mapping for the specified key once the call returns.
     *
     * @param key key whose mapping is to be removed from the dictionary
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for <tt>key</tt>.
     */
    default @Nullable V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the mapping for a key from this dictionary if it is present (optional operation).
     * More formally, if this dictionary contains a mapping from key <tt>k</tt> to value <tt>v</tt>
     * such that <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is removed.  (The
     * map can contain at most one such mapping.)
     * <p>Returns the value to which this dictionary previously associated the key, or <tt>null</tt>
     * if the dictionary contained no mapping for the key.
     * <p>If this dictionary permits null values, then a return value of <tt>null</tt> does not
     * <i>necessarily</i> indicate that the dictionary contained no mapping for the key; it's also
     * possible that the dictionary explicitly mapped the key to <tt>null</tt>.
     * <p>The dictionary will not contain a mapping for the specified key once the call returns.
     *
     * @param key key whose mapping is to be removed from the dictionary
     * @return the previous optional value associated with <tt>key</tt>.
     */
    default @NotNull Optional<V> removeOptional(K key) {
        return Optional.ofNullable(remove(key));
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param consumer the consumer.
     */
    default void forEach(@NotNull BiConsumer<@NotNull ? super K, @NotNull ? super V> consumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param <T>      the argument's type.
     * @param argument the argument.
     * @param consumer the consumer.
     */
    default <T> void forEach(
            @NotNull T argument,
            @NotNull TripleConsumer<@NotNull ? super T, @NotNull ? super K, @NotNull ? super V> consumer
    ) {
        throw new UnsupportedOperationException();
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param <F>      the first argument's type.
     * @param <S>      the second argument's type.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the consumer.
     */
    default <F, S> void forEach(
            @NotNull F first,
            @NotNull S second,
            @NotNull FourObjectConsumer<@NotNull ? super F, @NotNull ? super S, @NotNull ? super K, @NotNull ? super V> consumer
    ) {
        throw new UnsupportedOperationException();
    }
}


