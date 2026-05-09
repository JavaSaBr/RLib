package javasabr.rlib.reusable.pool;

import javasabr.rlib.reusable.Reusable;

/**
 * A pool specifically for {@link Reusable} objects.
 *
 * @param <E> the reusable element type
 * @since 10.0.0
 */
public interface ReusablePool<E extends Reusable> extends Pool<E> {}
