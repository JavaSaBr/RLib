package com.ss.rlib.common.util.array;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.*;

/**
 * The interface with methods to manage threadsafe for the Arrays.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public interface ConcurrentArray<E> extends Array<E> {

    /**
     * Create a new concurrent array for the element's type.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the new concurrent array.
     */
    static <T> @NotNull ConcurrentArray<T> ofType(@NotNull Class<? super T> type) {
        return ArrayFactory.newConcurrentStampedLockArray(type);
    }

    /**
     * Create a supplier to create new arrays.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the supplier.
     */
    static <T> @NotNull Supplier<ConcurrentArray<T>> supplier(@NotNull Class<? super T> type) {
        return () -> ArrayFactory.newConcurrentStampedLockArray(type);
    }

    /**
     * Create a function to create new arrays.
     *
     * @param <T>  the element's type.
     * @return the supplier.
     */
    static <T> @NotNull Function<Class<? super T>, ConcurrentArray<T>> function() {
        return ArrayFactory::newConcurrentStampedLockArray;
    }

    /**
     * Create a function to create new arrays.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the supplier.
     */
    static <T> @NotNull Function<Class<? super T>, ConcurrentArray<T>> function(@NotNull Class<?> type) {
        return ArrayFactory::newConcurrentStampedLockArray;
    }

    /**
     * Lock this array for reading.
     *
     * @return the stamp of read lock or 0.
     */
    default long readLock() {
        throw new UnsupportedOperationException();
    }

    /**
     * Unlock the read lock.
     *
     * @param stamp the stamp of read lock.
     */
    default void readUnlock(long stamp) {
        throw new UnsupportedOperationException();
    }

    /**
     * Try to optimistic read.
     *
     * @return the stamp of optimistic read or 0 if it was failed.
     */
    default long tryOptimisticRead() {
        throw new UnsupportedOperationException();
    }

    /**
     * Validate this stamp.
     *
     * @param stamp the stamp.
     * @return true is this stamp is valid.
     */
    default boolean validate(long stamp) {
        throw new UnsupportedOperationException();
    }

    /**
     * Lock this array for writing.
     *
     * @return the stamp of write lock or 0.
     */
    default long writeLock() {
        throw new UnsupportedOperationException();
    }

    /**
     * Unlock the write lock.
     *
     * @param stamp the stamp of write lock.
     */
    default void writeUnlock(long stamp) {
        throw new UnsupportedOperationException();
    }

    /**
     * Execute the function in read lock of this array.
     *
     * @param function the function.
     * @return this array.
     */
    default @NotNull ConcurrentArray<E> runInReadLock(@NotNull Consumer<ConcurrentArray<E>> function) {

        long stamp = readLock();
        try {
            function.accept(this);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply the function to each element in the {@link #readLock()} block.
     *
     * @param consumer the consumer.
     * @return this array.
     */
    default @NotNull ConcurrentArray<E> forEachInReadLock(@NotNull Consumer<? super E> consumer) {

        long stamp = readLock();
        try {
            forEach(consumer);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply the function to each element in the {@link #readLock()} block.
     *
     * @param <T>      the argument's type.
     * @param argument the argument.
     * @param function the function.
     * @return this array.
     */
    default <T> @NotNull ConcurrentArray<E> forEachInReadLock(
            @Nullable T argument,
            @NotNull BiConsumer<E, T> function
    ) {

        long stamp = readLock();
        try {
            forEach(argument, function);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply the function to each converted element in the {@link #readLock()} block.
     *
     * @param <T>       the argument's type.
     * @param <C>       the converted type.
     * @param argument  the argument.
     * @param converter the converter from T to C.
     * @param function  the function.
     * @return this array.
     */
    default <T, C> ConcurrentArray<E> forEachInReadLock(
            @Nullable T argument,
            @NotNull Function<E, C> converter,
            @NotNull BiConsumer<C, T> function
    ) {

        long stamp = readLock();
        try {
            forEach(argument, converter, function);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute the function in read lock of this array.
     *
     * @param function the function.
     * @return some result.
     */
    default @Nullable E getInReadLock(@NotNull Function<ConcurrentArray<E>, E> function) {
        long stamp = readLock();
        try {
            return function.apply(this);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Execute the function and get a result of the function in write lock of the array.
     *
     * @param function the function.
     * @param <R>      the result's type.
     * @return the result of the function.
     */
    default <R> @Nullable R getInWriteLock(@NotNull Function<@NotNull Array<E>, @Nullable R> function) {
        long stamp = writeLock();
        try {
            return function.apply(this);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Execute the function in read lock of this array.
     *
     * @param <F>      the argument's type.
     * @param argument the argument.
     * @param function the function.
     * @return this array.
     */
    default <F> ConcurrentArray<E> runInReadLock(
        @Nullable F argument,
        @NotNull BiConsumer<ConcurrentArray<E>, F> function
    ) {

        long stamp = readLock();
        try {
            function.accept(this, argument);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute the function in write lock of this array.
     *
     * @param function the function.
     * @return this array.
     */
    default @NotNull ConcurrentArray<E> runInWriteLock(@NotNull Consumer<ConcurrentArray<E>> function) {

        long stamp = writeLock();
        try {
            function.accept(this);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute the function in write lock of this array.
     *
     * @param <F>      the argument's type.
     * @param argument the argument.
     * @param function the function.
     * @return this array.
     */
    default <F> ConcurrentArray<E> runInWriteLock(
            @NotNull F argument,
            @NotNull BiConsumer<@NotNull ConcurrentArray<E>, @NotNull F> function
    ) {

        long stamp = writeLock();
        try {
            function.accept(this, argument);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Search an element using the condition in the {@link #readLock()} block.
     *
     * @param <T>       the argument's type.
     * @param argument  the argument.
     * @param predicate the condition.
     * @return the found element or null.
     */
    default <T> @Nullable E anyMatchInReadLock(@Nullable T argument, @NotNull BiPredicate<E, T> predicate) {

        if (isEmpty()) {
            return null;
        }

        long stamp = readLock();
        try {

            for (E element : array()) {
                if (element == null) {
                    break;
                } else if (predicate.test(element, argument)) {
                    return element;
                }
            }

        } finally {
            readUnlock(stamp);
        }

        return null;
    }
}
