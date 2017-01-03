package rlib.util.pools.impl;

/**
 * The final version of the {@link ConcurrentAtomicARSWLockPool}.
 *
 * @author JavaSaBr
 */
public class FinalConcurrentAtomicARSWLockPool<E> extends ConcurrentAtomicARSWLockPool<E> {

    public FinalConcurrentAtomicARSWLockPool(final Class<?> type) {
        super(type);
    }
}
