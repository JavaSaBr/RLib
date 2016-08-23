package rlib.util.pools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The interface for implementing a pool for storing and reusing any objects.
 *
 * @author JavaSaBr
 */
public interface Pool<E> {

    /**
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
    default E take(@NotNull final Supplier<E> factory) {
        final E take = take();
        return take != null ? take : factory.get();
    }

    /**
     * Takes an object from this pool.
     *
     * @param argument the argument for the factory function.
     * @param factory  the factory for creating new object if this pool is empty.
     * @return taken object.
     */
    default <T> E take(@Nullable final T argument, @NotNull final Function<T, E> factory) {
        final E take = take();
        return take != null ? take : factory.apply(argument);
    }

    /**
     * Takes an object from this pool.
     *
     * @param first   the first argument for the factory function.
     * @param second  the second argument for the factory function.
     * @param factory the factory for creating new object if this pool is empty.
     * @return taken object.
     */
    default <F, S> E take(@Nullable final F first, @Nullable final S second, @NotNull final BiFunction<F, S, E> factory) {
        final E take = take();
        return take != null ? take : factory.apply(first, second);
    }
}
