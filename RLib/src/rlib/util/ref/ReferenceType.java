package rlib.util.ref;

import rlib.util.pools.FoldablePool;
import rlib.util.pools.PoolFactory;

/**
 * Перечисление типов ссылок.
 * 
 * @author Ronn
 */
public enum ReferenceType {

	FLOAT,
	DOUBLE,
	CHAR,
	OBJECT,
	BYTE,
	SHORT,
	LONG,
	INTEGER;

	/** пул ссылок */
	private final FoldablePool<Reference> pool;

	private ReferenceType() {
		this.pool = PoolFactory.newAtomicFoldablePool(Reference.class);
	}

	/**
	 * @param wrap складировать ссылку.
	 */
	protected void put(Reference wrap) {
		pool.put(wrap);
	}

	/**
	 * @return достать использованную ссылку.
	 */
	protected Reference take() {
		return pool.take();
	}
}
