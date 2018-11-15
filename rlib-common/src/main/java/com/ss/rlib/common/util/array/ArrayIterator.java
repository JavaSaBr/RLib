package com.ss.rlib.common.util.array;

import java.util.Iterator;

/**
 * The interface for implementing an iterator for {@link Array}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public interface ArrayIterator<E> extends Iterator<E> {

    /**
     * Removes the current element using reordering.
     */
    void fastRemove();

    /**
     * Index int.
     *
     * @return the current position of this {@link Array}.
     */
    int index();
}
