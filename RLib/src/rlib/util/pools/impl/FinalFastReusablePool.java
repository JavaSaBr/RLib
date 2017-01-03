package rlib.util.pools.impl;

import rlib.util.pools.Reusable;

/**
 * The final version of the {@link FastReusablePool}.
 *
 * @author JavaSaBr
 */
public class FinalFastReusablePool<E extends Reusable> extends FastReusablePool<E> {

    public FinalFastReusablePool(final Class<?> type) {
        super(type);
    }
}
