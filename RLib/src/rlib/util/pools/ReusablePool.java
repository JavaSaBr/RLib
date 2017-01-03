package rlib.util.pools;

/**
 * The interface for implementing a pool for only {@link Reusable} objects.
 *
 * @author JavaSaBr
 */
public interface ReusablePool<E extends Reusable> extends Pool<E> {
}
