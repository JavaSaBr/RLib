package javasabr.rlib.collection;

import javasabr.rlib.function.NotNullFunction;
import org.jetbrains.annotations.NotNull;

/**
 * The interface for implementing a key-value dictionary.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public interface Dictionary<K, V> extends Iterable<V> {

    /**
     * Replace the all values using the function.
     */
    default void replace(@NotNull NotNullFunction<? super V, V> function) {
        throw new UnsupportedOperationException();
    }

    default void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if this dictionary contains the value.
     */
    default boolean containsValue(@NotNull V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if this dictionary is empty
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Put all data to from this dictionary to the provided.
     */
    void copyTo(@NotNull Dictionary<? super K, ? super V> dictionary);

    /**
     * Put all data from the provided dictionary to this.
     */
    default void put(@NotNull Dictionary<K, V> dictionary) {
        dictionary.copyTo(this);
    }

    /**
     * @return the number of key-value mappings in this dictionary.
     */
    default int size() {
        throw new UnsupportedOperationException();
    }
}
