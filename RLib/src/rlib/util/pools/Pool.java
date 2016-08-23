package rlib.util.pools;

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
    public boolean isEmpty();

    /**
     * Puts the object to this pool.
     *
     * @param object the object.
     */
    public void put(E object);

    /**
     * Removes the object from this pool.
     *
     * @param object the object.
     */
    public void remove(E object);

    /**
     * Takes an object from this pool.
     *
     * @return taken object or null is this pool is empty.
     */
    public E take();

    /**
     * Takes an object from this pool.
     *
     * @param factory the factory for creating new object if this pool is empty.
     * @return taken object.
     */
    public default E take(final Supplier<E> factory) {
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
    public default <T> E take(final T argument, final Function<T, E> factory) {
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
    public default <F, S> E take(final F first, S second, final BiFunction<F, S, E> factory) {
        final E take = take();
        return take != null ? take : factory.apply(first, second);
    }
}
