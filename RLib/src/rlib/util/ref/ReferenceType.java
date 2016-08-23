package rlib.util.ref;

import java.util.function.Supplier;

import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

/**
 * Перечисление типов ссылок.
 *
 * @author JavaSaBr
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
        this.pool = PoolFactory.newConcurrentPrimitiveAtomicARSWLockReusablePool(Reference.class);
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

    /**
     * @return достать использованную ссылку.
     */
    protected Reference take(Supplier<Reference> factory) {
        return pool.take(factory);
    }
}
