package rlib.concurrent.atomic;

import rlib.util.pools.Foldable;

/**
 * @author Ronn
 */
public class AtomicReference<V> extends java.util.concurrent.atomic.AtomicReference<V> implements Foldable {

	private static final long serialVersionUID = -4058945159519762615L;

	public AtomicReference() {
	}

	public AtomicReference(final V initialValue) {
		super(initialValue);
	}
}
