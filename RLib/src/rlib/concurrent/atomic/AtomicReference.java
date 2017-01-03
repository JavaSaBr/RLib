package rlib.concurrent.atomic;

import rlib.util.pools.Reusable;

/**
 * The atomic reference with additional methods.
 *
 * @author JavaSaBr
 */
public final class AtomicReference<V> extends java.util.concurrent.atomic.AtomicReference<V> implements Reusable {

    private static final long serialVersionUID = -4058945159519762615L;

    public AtomicReference() {
    }

    public AtomicReference(final V initialValue) {
        super(initialValue);
    }
}
