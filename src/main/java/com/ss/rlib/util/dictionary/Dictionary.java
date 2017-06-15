package com.ss.rlib.util.dictionary;

import static com.ss.rlib.util.array.ArrayFactory.newArray;

import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * The interface for implementing a key-value dictionary.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public interface Dictionary<K, V> extends Iterable<V>, Reusable {

    /**
     * Replace the all values using the function.
     *
     * @param function the function
     */
    default void apply(@NotNull final Function<? super V, V> function) {
        throw new UnsupportedOperationException();
    }

    /**
     * Clears this dictionary.
     */
    default void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns <tt>true</tt> if this dictionary maps one or more keys to the specified value.  More
     * formally, returns <tt>true</tt> if and only if this dictionary contains at least one mapping
     * to a value <tt>v</tt> such that <tt>(value==null ? v==null : value.equals(v))</tt>.  This
     * operation will probably require time linear in the dictionary size for most implementations
     * of the <tt>Dictionary</tt> interface.
     *
     * @param value value whose presence in this dictionary is to be tested.
     * @return <tt>true</tt> if this dictionary maps one or more keys to the specified value.
     */
    default boolean containsValue(@NotNull final V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void free() {
        clear();
    }

    /**
     * Gets type.
     *
     * @return the type of the dictionary.
     */
    @NotNull
    default DictionaryType getType() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns <tt>true</tt> if this dictionary contains no key-value mappings.
     *
     * @return <tt>true</tt> if this dictionary contains no key-value mappings
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Moves all data from this dictionary to the dictionary.
     *
     * @param dictionary the dictionary for moving data.
     */
    default void moveTo(@NotNull final Dictionary<? super K, ? super V> dictionary) {
        if (getType() != dictionary.getType()) {
            throw new IllegalArgumentException("incorrect table type.");
        }
    }

    /**
     * Puts all data from the dictionary to this dictionary.
     *
     * @param dictionary the dictionary with new data.
     */
    default void put(@NotNull final Dictionary<K, V> dictionary) {
        dictionary.moveTo(this);
    }

    /**
     * Returns the number of key-value mappings in this dictionary.  If the dictionary contains more
     * than <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this dictionary.
     */
    default int size() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets all values from this dictionary.
     *
     * @param container the container for storing the values.
     * @return the container with all values from this dictionary.
     */
    @NotNull
    default Array<V> values(@NotNull final Array<V> container) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets all values from this dictionary.
     *
     * @param type the type of values in this dictionary.
     * @return the array with all values from this dictionary.
     */
    @NotNull
    default Array<V> values(@NotNull final Class<V> type) {
        return values(ArrayFactory.newArray(type, size()));
    }
}
