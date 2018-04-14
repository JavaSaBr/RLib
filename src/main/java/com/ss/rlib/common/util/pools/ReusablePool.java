package com.ss.rlib.common.util.pools;

/**
 * The interface for implementing a pool for only {@link Reusable} objects.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public interface ReusablePool<E extends Reusable> extends Pool<E> {
}
