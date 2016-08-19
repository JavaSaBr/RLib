package rlib.util.array;

import java.util.Comparator;

/**
 * The interface for implementing a comparator for Array.
 *
 * @author JavaSaBr
 */
public interface ArrayComparator<T> extends Comparator<T> {

    @Override
    public default int compare(final T first, final T second) {

        if (first == null) {
            return 1;
        } else if (second == null) {
            return -1;
        }

        return compareImpl(first, second);
    }

    /**
     * Compare the two objects.
     *
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    public int compareImpl(T first, T second);
}
