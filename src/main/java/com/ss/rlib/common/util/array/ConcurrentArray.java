package com.ss.rlib.common.util.array;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The interface with methods to manage threadsafe for the Arrays.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public interface ConcurrentArray<E> extends Array<E> {

    /**
     * Creates a new concurrent array for the element's type.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the new concurrent array.
     */
    static <T> @NotNull ConcurrentArray<T> of(@NotNull Class<?> type) {
        return ArrayFactory.newConcurrentStampedLockArray(type);
    }

    /**
     * Create a supplier which creates new arrays.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the supplier.
     */
    static <T> @NotNull Supplier<ConcurrentArray<T>> supplier(@NotNull Class<?> type) {
        return () -> ArrayFactory.newConcurrentStampedLockArray(type);
    }

    /**
     * Create a function which creates new arrays.
     *
     * @param type the element's type.
     * @param <T>  the element's type.
     * @return the supplier.
     */
    static <T> @NotNull Function<Class<?>, ConcurrentArray<T>> function(@NotNull Class<?> type) {
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
     * Execute the function in read lock of this array.
     *
     * @param <F>      the argument's type.
     * @param argument the argument.
     * @param function the function.
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
     */
    default <F> ConcurrentArray<E> runInWriteLock(
            @Nullable F argument,
            @NotNull BiConsumer<ConcurrentArray<E>, F> function
    ) {

        long stamp = writeLock();
        try {
            function.accept(this, argument);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }
}
