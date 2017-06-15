package com.ss.rlib.util.pools.impl;

/**
 * The final version of the {@link FastPool}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FinalFastPool<E> extends FastPool<E> {

    /**
     * Instantiates a new Final fast pool.
     *
     * @param type the type
     */
    public FinalFastPool(final Class<?> type) {
        super(type);
    }
}
