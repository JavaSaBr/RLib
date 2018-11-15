package com.ss.rlib.common.util.pools;

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
     * Return true if this pool is empty.
     *
     * @return true if this pool is empty.
     */
    boolean isEmpty();

    /**
     * Put the object to this pool.
     *
     * @param object the object.
     */
    void put(@NotNull E object);

    /**
     * Remove the object from this pool.
     *
     * @param object the object.
     */
    void remove(@NotNull E object);

    /**
     * Take an object from this pool.
     *
     * @return taken object or null if this pool is empty.
     */
    @Nullable E take();

    /**
     * Take an object from this pool or create a new object.
     *
     * @param factory the factory to create new object if this pool is empty.
     * @return taken or created object.
     */
    default @NotNull E take(@NotNull Supplier<@NotNull E> factory) {
        E take = take();
        return take != null ? take : factory.get();
    }

    /**
     * Take an object from this pool or create a new object.
     *
     * @param argument the argument.
     * @param factory  the factory to create new object if this pool is empty.
     * @param <T>      the argument's type.
     * @return taken or created object.
     */
    default <T> @NotNull E take(@NotNull T argument, @NotNull Function<@NotNull T, @NotNull E> factory) {
        E take = take();
        return take != null ? take : factory.apply(argument);
    }

    /**
     * Take an object from this pool or create a new object.
     *
     * @param argument the argument.
     * @param factory  the factory to create new object if this pool is empty.
     * @return taken or created object.
     */
    default @NotNull E take(long argument, @NotNull LongFunction<@NotNull E> factory) {
        E take = take();
        return take != null ? take : factory.apply(argument);
    }

    /**
     * Take an object from this pool or create a new object.
     *
     * @param first   the first argument.
     * @param second  the second argument.
     * @param factory the factory to create new object if this pool is empty.
     * @param <F>     the first argument's type.
     * @return taken or created object.
     */
    default <F> @NotNull E take(
            @NotNull F first,
            long second,
            @NotNull ObjectLongFunction<@NotNull F, @NotNull E> factory
    ) {
        E take = take();
        return take != null ? take : factory.apply(first, second);
    }

    /**
     * Take an object from this pool or create a new object.
     *
     * @param first   the first argument.
     * @param second  the second argument.
     * @param factory the factory to create new object if this pool is empty.
     * @param <F>     the first argument's type.
     * @param <S>     the second argument's type.
     * @return taken or created object.
     */
    default <F, S> @NotNull E take(
            @NotNull F first,
            @NotNull S second,
            @NotNull BiFunction<@NotNull F, @NotNull S, @NotNull E> factory
    ) {
        E take = take();
        return take != null ? take : factory.apply(first, second);
    }
}
