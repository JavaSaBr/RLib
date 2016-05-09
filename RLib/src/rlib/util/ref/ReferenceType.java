package rlib.util.ref;

import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

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

    /**
     * Пул ссылок.
     */
    private final ReusablePool<Reference> pool;

    private ReferenceType() {
        this.pool = PoolFactory.newAtomicFoldablePool(Reference.class);
    }

    /**
     * @param wrap складировать ссылку.
     */
    protected void put(final Reference wrap) {
        pool.put(wrap);
    }

    /**
     * @return достать использованную ссылку.
     */
    protected Reference take() {
        return pool.take();
    }
}
