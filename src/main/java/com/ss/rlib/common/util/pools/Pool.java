package com.ss.rlib.common.util.pools;

import static java.util.Objects.requireNonNull;
import com.ss.rlib.common.function.ObjectLongFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

/**
 * The interface for implementing a pool for storing and reusing any objects.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public interface Pool<E> {

    /**
     * Is empty boolean.
     *
     * @return true if this pool is empty.
     */
    boolean isEmpty();

    /**
     * Puts the object to this pool.
     *
     * @param object the object.
     */
    void put(@NotNull E object);

    /**
     * Removes the object from this pool.
     *
     * @param object the object.
     */
    void remove(@NotNull E object);

    /**
     * Takes an object from this pool.
     *
     * @return taken object or null is this pool is empty.
     */
    @Nullable
    E take();

    /**
     * Takes an object from this pool.
     *
     * @param factory the factory for creating new object if this pool is empty.
     * @return taken object.
     */
    @NotNull
    default E take(@NotNull final Supplier<E> factory) {
        final E take = take();
        return take != null ? take : factory.get();
    }

    /**
     * Takes an object from this pool.
     *
     * @param <T>      the type parameter
     * @param argument the argument for the factory function.
     * @param factory  the factory for creating new object if this pool is empty.
     * @return taken object.
     */
    @NotNull
    default <T> E take(@Nullable final T argument, @NotNull final Function<T, E> factory) {
        final E take = take();
        return take != null ? take : factory.apply(argument);
    }

    /**
     * Takes an object from this pool.
     *
     * @param argument the argument for the factory function.
     * @param factory  the factory for creating new object if this pool is empty.
     * @return taken object.
     */
    @NotNull
    default E take(final long argument, @NotNull final LongFunction<E> factory) {
        final E take = take();
        return take != null ? take : factory.apply(argument);
    }

    /**
     * Takes an object from this pool.
     *
     * @param <F>     the type parameter
     * @param first   the first argument for the factory function.
     * @param second  the second argument for the factory function.
     * @param factory the factory for creating new object if this pool is empty.
     * @return taken object.
     */
    @NotNull
    default <F> E take(@Nullable F first, final long second, @NotNull final ObjectLongFunction<F, E> factory) {
        final E take = take();
        return take != null ? take : requireNonNull(factory.apply(first, second));
    }

    /**
     * Takes an object from this pool.
     *
     * @param <F>     the type parameter
     * @param <S>     the type parameter
     * @param first   the first argument for the factory function.
     * @param second  the second argument for the factory function.
     * @param factory the factory for creating new object if this pool is empty.
     * @return taken object.
     */
    @NotNull
    default <F, S> E take(@Nullable final F first, @Nullable final S second, @NotNull final BiFunction<F, S, E> factory) {
        final E take = take();
        return take != null ? take : factory.apply(first, second);
    }
}
