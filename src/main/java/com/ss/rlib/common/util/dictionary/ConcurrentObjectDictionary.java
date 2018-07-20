package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.TripleConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * The interface with methods for supporting threadsafe for the {@link ObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public interface ConcurrentObjectDictionary<K, V> extends ObjectDictionary<K, V>, ConcurrentDictionary<K, V> {

    /**
     * Create a new concurrent object dictionary for the key's type and value's type.
     *
     * @param keyValueType the key's and value's type.
     * @param <T>          the key's and value's type.
     * @return the new concurrent object dictionary.
     */
    static <T> @NotNull ObjectDictionary<T, T> ofType(@NotNull Class<? super T> keyValueType) {
        return DictionaryFactory.newConcurrentAtomicObjectDictionary();
    }

    /**
     * Create a new concurrent object dictionary for the key's type and value's type.
     *
     * @param keyType   the key's type.
     * @param valueType the value's type.
     * @param <K>       the key's type.
     * @param <V>       the value's type.
     * @return the new concurrent object dictionary.
     */
    static <K, V> @NotNull ConcurrentObjectDictionary<K, V> ofType(
            @NotNull Class<? super K> keyType,
            @NotNull Class<? super V> valueType
    ) {
        return DictionaryFactory.newConcurrentAtomicObjectDictionary();
    }

    /**
     * Execute the function for this dictionary in the block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param consumer the function.
     * @return this dictionary.
     */
    default @NotNull ConcurrentObjectDictionary<K, V> runInWriteLock(
            @NotNull Consumer<@NotNull ConcurrentObjectDictionary<K, V>> consumer
    ) {

        long stamp = writeLock();
        try {
            consumer.accept(this);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute the function for this dictionary in the block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param key      the key value.
     * @param consumer the function.
     * @return this dictionary.
     */
    default @NotNull ConcurrentObjectDictionary<K, V> runInWriteLock(
            @NotNull K key,
            @NotNull BiConsumer<@NotNull ConcurrentObjectDictionary<K, V>, @NotNull K> consumer
    ) {

        long stamp = writeLock();
        try {
            consumer.accept(this, key);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute the function for this dictionary in the block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param key      the key value.
     * @param argument the additional argument,
     * @param consumer the function.
     * @param <F>      the argument's type.
     * @return this dictionary.
     */
    default <F> @NotNull ConcurrentObjectDictionary<K, V> runInWriteLock(
            @NotNull K key,
            @NotNull F argument,
            @NotNull TripleConsumer<@NotNull ConcurrentObjectDictionary<K, V>, @NotNull K, @NotNull F> consumer
    ) {

        long stamp = writeLock();
        try {
            consumer.accept(this, key, argument);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Get the value using a function from a dictionary in the block {@link
     * ConcurrentObjectDictionary#readLock()}.
     *
     * @param key      the key value.
     * @param function the function.
     * @return the result of the function.
     */
    default @Nullable V getInReadLock(
            @NotNull K key,
            @NotNull BiFunction<ConcurrentObjectDictionary<K, V>, @NotNull K, @NotNull V> function
    ) {
        long stamp = readLock();
        try {
            return function.apply(this, key);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Get the value using a function from a dictionary in the block {@link
     * ConcurrentObjectDictionary#writeLock()}.
     *
     * @param key      the key value.
     * @param function the function.
     * @return the result of the function.
     */
    default @Nullable V getInWriteLock(
            @NotNull K key,
            @NotNull BiFunction<ConcurrentObjectDictionary<K, V>, @NotNull K, @NotNull V> function
    ) {
        long stamp = writeLock();
        try {
            return function.apply(this, key);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param consumer the consumer.
     */
    default void forEachInReadLock(@NotNull BiConsumer<@NotNull ? super K, @NotNull ? super V> consumer) {
        long stamp = readLock();
        try {
            forEach(consumer);
        } finally {
            readUnlock(stamp);
        }
    }
}
