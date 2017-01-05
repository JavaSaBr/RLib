package rlib.util.array;

import java.util.Iterator;

/**
 * The interface for implementing an iterator for {@link Array}.
 *
 * @author JavaSaBr
 */
public interface ArrayIterator<E> extends Iterator<E> {

    /**
     * Removes the current element using reordering.
     */
    void fastRemove();

    /**
     * @return the current position of this {@link Array}.
     */
    int index();
}
