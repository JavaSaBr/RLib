package rlib.util.array.impl;

import rlib.util.array.Array;

/**
 * The final version of the array iterator.
 *
 * @author JavaSaBr
 */
public class FinalArrayIterator<E> extends ArrayIteratorImpl<E> {

    public FinalArrayIterator(final Array<E> array) {
        super(array);
    }
}
