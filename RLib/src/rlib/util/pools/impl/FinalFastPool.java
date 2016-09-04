package rlib.util.pools.impl;

/**
 * The final version of the {@link FastPool}.
 *
 * @author JavaSaBr
 */
public class FinalFastPool<E> extends FastPool<E> {

    public FinalFastPool(final Class<?> type) {
        super(type);
    }
}
