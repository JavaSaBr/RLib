package rlib.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

import rlib.function.TripleConsumer;
import rlib.util.array.Array;

/**
 * The interface for implementing a key-value dictionary which using an object key.
 *
 * @author JavaSaBr
 */
public interface ObjectDictionary<K, V> extends Dictionary<K, V> {

    /**
     * Returns <tt>true</tt> if this dictionary contains a mapping for the specified key.  More
     * formally, returns <tt>true</tt> if and only if this dictionary contains a mapping for a key
     * <tt>k</tt> such that <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be at most
     * one such mapping.)
     *
     * @param key key whose presence in this dictionary is to be tested.
     * @return <tt>true</tt> if this dictionary contains a mapping for the specified key.
     */
    default boolean containsKey(@NotNull final K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this dictionary
     * contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value to which the specified key is mapped, or {@code null} if this dictionary
     * contains no mapping for the key.
     */
    @Nullable
    default V get(@NotNull final K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     */
    @Nullable
    default V get(@NotNull final K key, @NotNull final Supplier<V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     */
    @Nullable
    default V get(@NotNull final K key, @NotNull final Function<K, V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key      the key.
     * @param argument the additional argument.
     * @param factory  the factory.
     */
    @Nullable
    default <T> V get(@NotNull final K key, @Nullable final T argument, @NotNull final Function<T, V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets all keys of this dictionary.
     *
     * @param container the container for storing keys.
     * @return the array with all keys.
     */
    @NotNull
    default Array<K> keyArray(@NotNull final Array<K> container) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return массив ключей словаря.
     */
    @NotNull
    default Array<K> keyArray(@NotNull final Class<K> type) {
        throw new UnsupportedOperationException();
    }

    /**
     * Добавляет новое значение по указанному ключу, и если уже есть элемент с таким ключем,
     * возвращает его.
     *
     * @param key   ключ значения.
     * @param value вставляемое значение.
     */
    @Nullable
    default V put(@NotNull final K key, @Nullable final V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Удаляет значение по ключу.
     *
     * @param key ключ значения.
     */
    @Nullable
    default V remove(final K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Пробег по словарю с просмотром ключа и значения и дополнительным аргументом.
     *
     * @param argument дополнительный аргумент.
     * @param consumer функция обработки ключа и значения.
     */
    default <T> void forEach(@Nullable final T argument, @NotNull final TripleConsumer<K, V, T> consumer) {
        throw new UnsupportedOperationException();
    }
}
