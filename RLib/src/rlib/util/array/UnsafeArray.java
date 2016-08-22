package rlib.util.array;

import org.jetbrains.annotations.NotNull;

/**
 * The interface with unsafe methods for the Array.
 *
 * @author JavaSaBr.
 */
public interface UnsafeArray<E> extends Array<E> {

    /**
     * Prepare this array for the target size.
     */
    default void prepareForSize(final int size) {
        throw new UnsupportedOperationException();
    }

    /**
     * Add the new element without any checks.
     *
     * @param object the new element.
     * @return this array.
     */
    default Array<E> unsafeAdd(@NotNull final E object) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the element for the index without any checks.
     *
     * @param index the index.
     * @return the element or null.
     */
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
     */
    Array<E> trimToSize();
}
