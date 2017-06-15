package com.ss.rlib.util.pools.impl;

import com.ss.rlib.util.pools.Reusable;

/**
 * The final version of the {@link FastReusablePool}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FinalFastReusablePool<E extends Reusable> extends FastReusablePool<E> {

    /**
     * Instantiates a new Final fast reusable pool.
     *
     * @param type the type
     */
    public FinalFastReusablePool(final Class<?> type) {
        super(type);
    }
}
