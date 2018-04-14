package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.array.Array;

/**
 * The final version of the array iterator.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FinalArrayIterator<E> extends ArrayIteratorImpl<E> {

    /**
     * Instantiates a new Final array iterator.
     *
     * @param array the array
     */
    public FinalArrayIterator(final Array<E> array) {
        super(array);
    }
}
