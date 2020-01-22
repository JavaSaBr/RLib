package com.ss.rlib.common.util.array;

import com.ss.rlib.common.function.*;
import com.ss.rlib.common.util.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * The interface with methods to manage thread-safe access with arrays.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
public interface ConcurrentArray<E> extends Array<E> {

    @SafeVarargs
    static <T> @NotNull ConcurrentArray<T> of(@NotNull T... elements) {
        return ArrayFactory.newConcurrentStampedLockArray(elements);
    }

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
    static <T> @NotNull NotNullSupplier<ConcurrentArray<T>> supplier(@NotNull Class<? super T> type) {
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
        return aClass -> ArrayFactory.newConcurrentStampedLockArray(ClassUtils.<Class<T>>unsafeNNCast(type));
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
     * Apply a function to each element under {@link #readLock()} block.
     *
     * @param consumer the consumer.
     * @return this array.
     */
    default @NotNull ConcurrentArray<E> forEachInReadLock(@NotNull NotNullConsumer<? super E> consumer) {

        var stamp = readLock();
        try {
            forEach(consumer);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply a function to each element under {@link #readLock()} block.
     *
     * @param argument the argument.
     * @param function the function.
     * @param <T>      the argument's type.
     * @return this array.
     */
    default <T> @NotNull ConcurrentArray<E> forEachInReadLock(
        @NotNull T argument,
        @NotNull NotNullBiConsumer<T, ? super E> function
    ) {

        var stamp = readLock();
        try {
            forEach(argument, function);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply a function to each element under {@link #readLock()} block.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function.
     * @param <A>      the second argument's type.
     * @return this array.
     */
    default <A> @NotNull ConcurrentArray<E> forEachInReadLock(
        int first,
        @NotNull A second,
        @NotNull NotNullIntBiObjectConsumer<A, ? super E> consumer
    ) {

        var stamp = readLock();
        try {
            forEach(first, second, consumer);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply a function to each element under {@link #readLock()} block.
     *
     * @param first    the first argument.
     * @param second   the second argument.
     * @param consumer the function.
     * @param <F>      the firs argument's type.
     * @param <S>      the second argument's type.
     * @return this array.
     */
    default <F, S> @NotNull ConcurrentArray<E> forEachInReadLock(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullTripleConsumer<F, S, ? super E> consumer
    ) {

        var stamp = readLock();
        try {
            forEach(first, second, consumer);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply a function to each element under {@link #readLock()} block.
     *
     * @param argument the argument.
     * @param function the function.
     * @param <T>      the argument's type.
     * @return this array.
     */
    default <T> @NotNull ConcurrentArray<E> forEachInReadLockR(
        @NotNull T argument,
        @NotNull NotNullBiConsumer<? super E, T> function
    ) {

        var stamp = readLock();
        try {
            forEachR(argument, function);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply a function to each converted element under {@link #readLock()} block.
     *
     * @param argument  the argument.
     * @param converter the converter from T to C.
     * @param function  the function.
     * @param <T>       the argument's type.
     * @param <C>       the converted type.
     * @return this array.
     */
    default <T, C> @NotNull ConcurrentArray<E> forEachConvertedInReadLock(
        @NotNull T argument,
        @NotNull NotNullFunction<? super E, C> converter,
        @NotNull NotNullBiConsumer<T, C> function
    ) {

        var stamp = readLock();
        try {
            forEachConverted(argument, converter, function);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply a function to each converted element under {@link #readLock()} block.
     *
     * @param first     the first argument.
     * @param second    the second argument.
     * @param converter the converter from E to C.
     * @param function  the function.
     * @param <F>       the first argument's type.
     * @param <S>       the second argument's type.
     * @param <C>       the converted type.
     * @return this array.
     * @since 9.8.0
     */
    default <F, S, C> @NotNull ConcurrentArray<E> forEachConvertedInReadLock(
        @NotNull F first,
        @NotNull S second,
        @NotNull NotNullFunction<? super E, C> converter,
        @NotNull NotNullTripleConsumer<F, S, C> function
    ) {

        var stamp = readLock();
        try {
            forEachConverted(first, second, converter, function);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Apply a function to each element and converted argument under {@link #readLock()} block.
     *
     * @param argument  the argument.
     * @param converter the converter from T to C.
     * @param function  the function.
     * @param <T>       the argument's type.
     * @param <C>       the converted type.
     * @return this array.
     */
    default <T, C> @NotNull ConcurrentArray<E> forEachInReadLock(
        @NotNull T argument,
        @NotNull NotNullFunction<T, C> converter,
        @NotNull NotNullBiConsumer<C, E> function
    ) {

        var stamp = readLock();
        try {
            forEach(argument, converter, function);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute a function to get some result under {@link #readLock()} block.
     *
     * @param function the function.
     * @param <R>      the result's type.
     * @return the result from the function.
     */
    default <R> @Nullable R getInReadLock(@NotNull NotNullNullableFunction<ConcurrentArray<E>, R> function) {
        var stamp = readLock();
        try {
            return function.apply(this);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Execute a function to get some result under {@link #readLock()} block.
     *
     * @param arg      the argument for the function.
     * @param function the function.
     * @param <A>      the argument's type.
     * @param <R>      the result's type.
     * @return the result from the function.
     * @since 9.5.0
     */
    default <A, R> @Nullable R getInReadLock(
        @NotNull A arg,
        @NotNull NotNullNullableBiFunction<ConcurrentArray<E>, A, R> function
    ) {
        var stamp = readLock();
        try {
            return function.apply(this, arg);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Execute a function to get some result under {@link #readLock()} block.
     *
     * @param first    the first argument for the function.
     * @param second   the second argument for the function.
     * @param function the function.
     * @param <A>      the first argument's type.
     * @param <T>      the second argument's type.
     * @param <R>      the result's type.
     * @return the result from the function.
     * @since 9.9.0
     */
    default <A, T, R> @Nullable R getInReadLock(
        @NotNull A first,
        @NotNull T second,
        @NotNull NotNullNullableTripleFunction<ConcurrentArray<E>, A, T, R> function
    ) {
        var stamp = readLock();
        try {
            return function.apply(this, first, second);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Execute a function to get some result under {@link #writeLock()} block.
     *
     * @param function the function.
     * @param <R>      the result's type.
     * @return the result from the function.
     */
    default <R> @Nullable R getInWriteLock(@NotNull NotNullNullableFunction<ConcurrentArray<E>, R> function) {
        var stamp = writeLock();
        try {
            return function.apply(this);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Execute a function to get some result under {@link #writeLock()} block.
     *
     * @param argument      the argument for the function.
     * @param function the function.
     * @param <A>      the argument's type.
     * @param <R>      the result's type.
     * @return the result from the function.
     * @since 9.5.0
     */
    default <A, R> @Nullable R getInWriteLock(
        @NotNull A argument,
        @NotNull NotNullNullableBiFunction<ConcurrentArray<E>, A, R> function
    ) {
        var stamp = writeLock();
        try {
            return function.apply(this, argument);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Execute a function to get some result under {@link #writeLock()} block.
     *
     * @param first    the first argument for the function.
     * @param second   the second argument for the function.
     * @param function the function.
     * @param <A>      the first argument's type.
     * @param <T>      the second argument's type.
     * @param <R>      the result's type.
     * @return the result from the function.
     * @since 9.9.0
     */
    default <A, T, R> @Nullable R getInWriteLock(
        @NotNull A first,
        @NotNull T second,
        @NotNull NotNullNullableTripleFunction<ConcurrentArray<E>, A, T, R> function
    ) {
        var stamp = writeLock();
        try {
            return function.apply(this, first, second);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Execute a function under {@link #readLock()} block.
     *
     * @param function the function.
     * @return this array.
     */
    default @NotNull ConcurrentArray<E> runInReadLock(@NotNull NotNullConsumer<ConcurrentArray<E>> function) {

        var stamp = readLock();
        try {
            function.accept(this);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }


    /**
     * Execute a function under {@link #readLock()} block.
     *
     * @param <F>      the argument's type.
     * @param argument the argument.
     * @param function the function.
     * @return this array.
     */
    default <F> @NotNull ConcurrentArray<E> runInReadLock(
        @NotNull F argument,
        @NotNull NotNullBiConsumer<ConcurrentArray<E>, F> function
    ) {

        var stamp = readLock();
        try {
            function.accept(this, argument);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute a function under {@link #readLock()} block.
     *
     * @param <A>      the first argument's type.
     * @param <T>      the second argument's type.
     * @param first    the first argument.
     * @param second   the second argument.
     * @param function the function.
     * @return this array.
     * @since 9.9.0
     */
    default <A, T> @NotNull ConcurrentArray<E> runInReadLock(
        @NotNull A first,
        @NotNull T second,
        @NotNull NotNullTripleConsumer<ConcurrentArray<E>, A, T> function
    ) {

        var stamp = readLock();
        try {
            function.accept(this, first, second);
        } finally {
            readUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute a function under {@link #writeLock()} block.
     *
     * @param function the function.
     * @return this array.
     */
    default @NotNull ConcurrentArray<E> runInWriteLock(@NotNull NotNullConsumer<ConcurrentArray<E>> function) {

        var stamp = writeLock();
        try {
            function.accept(this);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute a function under {@link #writeLock()} block.
     *
     * @param <F>      the argument's type.
     * @param argument the argument.
     * @param function the function.
     * @return this array.
     */
    default <F> @NotNull ConcurrentArray<E> runInWriteLock(
        @NotNull F argument,
        @NotNull NotNullBiConsumer<ConcurrentArray<E>, F> function
    ) {

        var stamp = writeLock();
        try {
            function.accept(this, argument);
        } finally {
            writeUnlock(stamp);
        }

        return this;
    }

    /**
     * Execute a function under {@link #writeLock()} block.
     *
     * @param first    the first argument for the function.
     * @param second   the second argument for the function.
     * @param function the function.
     * @param <A>      the first argument's type.
     * @param <T>      the second argument's type.
     * @return this array.
     * @since 9.9.0
     */
    default <A, T> @NotNull ConcurrentArray<E> runInWriteLock(
        @NotNull A first,
        @NotNull T second,
        @NotNull NotNullTripleConsumer<ConcurrentArray<E>, A, T> function
    ) {
        var stamp = writeLock();
        try {
            function.accept(this, first, second);
        } finally {
            writeUnlock(stamp);
        }
        return this;
    }

    /**
     * Search an element using the condition under {@link #readLock()} block.
     *
     * @param argument the argument.
     * @param filter   the condition.
     * @return the found element or null.
     * @since 9.5.0
     */
    default @Nullable E findAnyInReadLock(int argument, @NotNull NotNullIntObjectPredicate<? super E> filter) {

        if (isEmpty()) {
            return null;
        }

        var stamp = readLock();
        try {
            return findAny(argument, filter);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Search an element using the condition by converted value under {@link #readLock()} block.
     *
     * @param argument  the argument.
     * @param converter the converted an element to another type.
     * @param filter    the condition.
     * @param <T>       the argument's type.
     * @param <C>       the converted element's type.
     * @return the found element or null.
     * @since 9.7.0
     */
    default <T, C> @Nullable E findAnyConvertedInReadLock(
        @NotNull T argument,
        @NotNull NotNullFunction<? super E, C> converter,
        @NotNull NotNullBiPredicate<T, C> filter
    ) {
        if (isEmpty()) {
            return null;
        }

        var stamp = readLock();
        try {
            return findAnyConverted(argument, converter, filter);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Search a converted element to int using the condition under {@link #readLock()} block.
     *
     * @param argument  the argument.
     * @param converter the converter element to int.
     * @param filter    the condition.
     * @return the found element or null.
     * @since 9.6.0
     */
    default @Nullable E findAnyConvertedToIntInReadLock(
        int argument,
        @NotNull NotNullFunctionInt<? super E> converter,
        @NotNull BiIntPredicate filter
    ) {
        if (isEmpty()) {
            return null;
        }

        var stamp = readLock();
        try {
            return findAnyConvertedToInt(argument, converter, filter);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Search a converted element to int using the condition under {@link #readLock()} block.
     *
     * @param argument        the argument.
     * @param firstConverter  the converter element to T.
     * @param secondConverter the converter element to int.
     * @param filter          the condition.
     * @param <T>             the first converted type.
     * @return the found element or null.
     * @since 9.7.0
     */
    default <T> @Nullable E findAnyConvertedToIntInReadLock(
        int argument,
        @NotNull NotNullFunction<? super E, T> firstConverter,
        @NotNull NotNullFunctionInt<T> secondConverter,
        @NotNull BiIntPredicate filter
    ) {
        if (isEmpty()) {
            return null;
        }

        var stamp = readLock();
        try {
            return findAnyConvertedToInt(argument, firstConverter, secondConverter, filter);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Search an element by condition under {@link #readLock()} block.
     *
     * @param <T>      the argument's type.
     * @param argument the argument.
     * @param filter   the condition.
     * @return true if there is at least an element for the condition.
     */
    default <T> boolean anyMatchInReadLock(@NotNull T argument, @NotNull NotNullBiPredicate<T, ? super E> filter) {

        if (isEmpty()) {
            return false;
        }

        var stamp = readLock();
        try {
            return anyMatch(argument, filter);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Search an element by condition under {@link #readLock()} block.
     *
     * @param argument the argument.
     * @param filter   the condition.
     * @return true if there is at least an element for the condition.
     */
    default boolean anyMatchInReadLock(int argument, @NotNull NotNullIntObjectPredicate<E> filter) {

        if (isEmpty()) {
            return false;
        }

        var stamp = readLock();
        try {
            return anyMatch(argument, filter);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Return true if there is at least a converted element for the condition under {@link #readLock()} block.
     *
     * @param argument  the argument.
     * @param converter the converter element to another type.
     * @param filter    the condition.
     * @param <T>       the argument's type.
     * @param <C>       the converted element's type.
     * @return true if there is at least an element for the condition.
     * @since 9.7.0
     */
    default <T, C> boolean anyMatchConvertedInReadLock(
        @NotNull T argument,
        @NotNull NotNullFunction<? super E, C> converter,
        @NotNull NotNullBiPredicate<T, C> filter
    ) {
        if (isEmpty()) {
            return false;
        }

        var stamp = readLock();
        try {
            return anyMatchConverted(argument, converter, filter);
        } finally {
            readUnlock(stamp);
        }
    }

    /**
     * Removes all of the elements of this collection that satisfy the given predicate under {@link #writeLock()} block.
     *
     * @param filter the predicate which returns {@code true} for elements to be removed.
     * @return {@code true} if any elements were removed.
     */
    default boolean removeIfInWriteLock(@NotNull NotNullPredicate<? super E> filter) {
        var stamp = writeLock();
        try {
            return removeIf(filter);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Removes all of the elements of this collection that satisfy the given predicate under {@link #writeLock()} block.
     *
     * @param argument the additional argument.
     * @param filter   the predicate which returns {@code true} for elements to be removed.
     * @param <A>      the argument's type.
     * @return {@code true} if any elements were removed.
     */
    default <A> boolean removeIfInWriteLock(@NotNull A argument, @NotNull NotNullBiPredicate<A, ? super E> filter) {
        var stamp = writeLock();
        try {
            return removeIf(argument, filter);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Removes all of the elements of this collection that satisfy the given predicate under {@link #writeLock()} block.
     *
     * @param argument  the additional argument.
     * @param converter the converter of the argument.
     * @param filter    the predicate which returns {@code true} for elements to be removed.
     * @param <A>       the argument's type.
     * @param <B>       the argument converted type.
     * @return {@code true} if any elements were removed.
     */
    default <A, B> boolean removeIfInWriteLock(
        @NotNull A argument,
        @NotNull NotNullFunction<A, B> converter,
        @NotNull NotNullBiPredicate<B, ? super E> filter
    ) {
        var stamp = writeLock();
        try {
            return removeIf(argument, converter, filter);
        } finally {
            writeUnlock(stamp);
        }
    }

    /**
     * Removes all of the elements of this collection that satisfy the given predicate.
     *
     * @param argument  the additional argument.
     * @param converter the converter of the elements.
     * @param filter    the predicate which returns {@code true} for elements to be removed.
     * @param <A>       the argument's type.
     * @param <B>       the element converted type.
     * @return {@code true} if any elements were removed.
     * @since 9.6.0
     */
    default <A, B> boolean removeIfConvertedInWriteLock(
        @NotNull A argument,
        @NotNull NotNullFunction<? super E, B> converter,
        @NotNull NotNullBiPredicate<A, B> filter
    ) {
        var stamp = writeLock();
        try {
            return removeIfConverted(argument, converter, filter);
        } finally {
            writeUnlock(stamp);
        }
    }
}
