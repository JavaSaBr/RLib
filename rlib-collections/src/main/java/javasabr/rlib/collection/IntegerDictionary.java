package javasabr.rlib.collection;
import javasabr.rlib.function.NotNullFunction;
import javasabr.rlib.function.NotNullIntFunction;
import javasabr.rlib.function.NotNullSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement a dictionary which uses int as key.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public interface IntegerDictionary<V> extends Dictionary<IntKey, V> {

    /**
     * @return true if this dictionary contains a mapping for the specified key.
     */
    default boolean containsKey(int key) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the value to which the specified key is mapped, or {@code null} if this dictionary contains no mapping for the key.
     */
    default @Nullable V get(final int key) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the stored value by the key or a new computed value.
     */
    default @NotNull V getOrCompute(int key, @NotNull NotNullSupplier<V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the stored value by the key or a new computed value.
     */
    default @NotNull V getOrCompute(int key, @NotNull NotNullIntFunction<V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the stored value by the key or a new computed value.
     */
    default <T> @Nullable V getOrCompute(int key, @NotNull T arg, @NotNull NotNullFunction<T, V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the previous value for the key or null.
     */
    default @Nullable V put(int key, @NotNull V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the previous value for the key or null.
     */
    default @Nullable V remove(int key) {
        throw new UnsupportedOperationException();
    }
}
