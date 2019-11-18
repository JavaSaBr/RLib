package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.function.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface with methods for supporting thread-safe for the {@link ObjectDictionary}.
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
        return DictionaryFactory.newConcurrentStampedLockObjectDictionary();
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
        return DictionaryFactory.newConcurrentStampedLockObjectDictionary();
    }

    /**
     * Execute a function for this dictionary under block {@link ConcurrentObjectDictionary#readLock()}.
     *
     * @param argument the argument.
     * @param consumer the function.
     * @param <A>      the argument's type.
     * @return this dictionary.
     */
    default <A> @NotNull ConcurrentObjectDictionary<K, V> runInReadLock(
        @NotNull A argument,
        @NotNull NotNullBiConsumer<ConcurrentObjectDictionary<K, V>, A> consumer
    ) {

        var stamp = readLock();
        try {
            consumer.accept(this, argument);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute a function for this dictionary under block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param consumer the function.
     * @return this dictionary.
     */
    default @NotNull ConcurrentObjectDictionary<K, V> runInWriteLock(
        @NotNull NotNullConsumer<ConcurrentObjectDictionary<K, V>> consumer
    ) {

        var stamp = writeLock();
        try {
            consumer.accept(this);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute a function for this dictionary under block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param argument the argument.
     * @param consumer the function.
     * @param <A>      the argument's type.
     * @return this dictionary.
     */
    default <A> @NotNull ConcurrentObjectDictionary<K, V> runInWriteLock(
        @NotNull A argument,
        @NotNull NotNullBiConsumer<ConcurrentObjectDictionary<K, V>, A> consumer
    ) {

        var stamp = writeLock();
        try {
            consumer.accept(this, argument);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute a function for this dictionary under block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function.
     * @param <F>      the first argument's type.
     * @param <S>      the second argument's type.
     * @return this dictionary.
     */
    default <F, S> @NotNull ConcurrentObjectDictionary<K, V> runInWriteLock(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullTripleConsumer<ConcurrentObjectDictionary<K, V>, F, S> consumer
    ) {

        var stamp = writeLock();
        try {
            consumer.accept(this, first, second);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Get the value from a function for this dictionary under block {@link ConcurrentObjectDictionary#readLock()}.
     *
     * @param argument the argument.
     * @param function the function.
     * @param <A>      the argument's type.
     * @param <R>      the result's type.
     * @return the result of the function.
     */
    default <A, R> @Nullable R getInReadLock(
        @NotNull A argument,
        @NotNull NotNullNullableBiFunction<ConcurrentObjectDictionary<K, V>, A, R> function
    ) {
        var stamp = readLock();
        try {
            return function.apply(this, argument);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Get the value from a function for this dictionary under block {@link ConcurrentObjectDictionary#readLock()}.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     * @param <F>      the first argument's type.
     * @param <S>      the second argument's type.
     * @param <R>      the result's type.
     * @return the result of the function.
     */
    default <F, S, R> @Nullable R getInReadLock(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullNullableTripleFunction<ConcurrentObjectDictionary<K, V>, F, S, R> function
    ) {
        var stamp = readLock();
        try {
            return function.apply(this, first, second);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Get the value from a function for this dictionary under block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param argument the argument.
     * @param function the function.
     * @param <A>      the argument's type.
     * @param <R>      the result's type.
     * @return the result of the function.
     */
    default <A, R> @Nullable R getInWriteLock(
        @NotNull A argument,
        @NotNull NotNullNullableBiFunction<ConcurrentObjectDictionary<K, V>, A, R> function
    ) {
        var stamp = writeLock();
        try {
            return function.apply(this, argument);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Get the value from a function for this dictionary under block {@link ConcurrentObjectDictionary#writeLock()}.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     * @param <F>      the first argument's type.
     * @param <S>      the second argument's type.
     * @param <R>      the result's type.
     * @return the result of the function.
     */
    default <F, S, R> @Nullable R getInWriteLock(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullNullableTripleFunction<ConcurrentObjectDictionary<K, V>, F, S, R> function
    ) {
        var stamp = writeLock();
        try {
            return function.apply(this, first, second);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param consumer the consumer.
     */
    default void forEachInReadLock(@NotNull NotNullBiConsumer<? super K, ? super V> consumer) {
        var stamp = readLock();
        try {
            forEach(consumer);
        } finally {
            readUnlock(stamp);
        }
    }
}
