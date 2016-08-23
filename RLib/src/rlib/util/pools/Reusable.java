package rlib.util.pools;

/**
 * The interface for implementing reusable objects. You can use reusable objects with {@link
 * ReusablePool}.
 *
 * @author JavaSaBr
 */
public interface Reusable {

    /**
     * Cleanup this object before storing to a pool.
     */
    public default void free() {
    }

    /**
     * Prepares to reuse this object before taking from a pool.
     */
    public default void reuse() {
    }

    /**
     * Stores this object to a pool.
     */
    public default void release() {
    }
}
