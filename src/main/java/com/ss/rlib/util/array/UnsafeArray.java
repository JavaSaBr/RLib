package com.ss.rlib.util.array;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface with unsafe methods for the Array.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public interface UnsafeArray<E> extends Array<E> {

    /**
     * Prepare this array for the target size.
     *
     * @param size the size
     */
    default void prepareForSize(final int size) {
        throw new UnsupportedOperationException();
    }

    /**
     * Add the new element without any checks.
     *
     * @param object the new element.
     * @return true if this array was changed.
     */
    default boolean unsafeAdd(@NotNull final E object) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the element for the index without any checks.
     *
     * @param index the index.
     * @return the element or null.
     */
    @Nullable
    default E unsafeGet(final int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * Set the element for the index without any checks.
     *
     * @param index   the index.
     * @param element the element.
     */
    default void unsafeSet(final int index, @NotNull final E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Reduce wrapped array to size of this array.
     *
     * @return the array
     */
    Array<E> trimToSize();
}
