package rlib.util.wraps;

import rlib.util.pools.FoldablePool;
import rlib.util.pools.Pools;

/**
 * Перечисление типов оберток.
 * 
 * @author Ronn
 */
public enum WrapType {

	FLOAT,
	DOUBLE,
	CHAR,
	OBJECT,
	BYTE,
	SHORT,
	LONG,
	INTEGER;

	/** пул оберток */
	private final FoldablePool<Wrap> pool;

	private WrapType() {
		this.pool = Pools.newConcurrentFoldablePool(Wrap.class);
	}

	/**
	 * @param wrap складировать экземпляр.
	 */
	protected void put(Wrap wrap) {
		pool.put(wrap);
	}

	/**
	 * @return достать экземпляр.
	 */
	protected Wrap take() {
		return pool.take();
	}
}
