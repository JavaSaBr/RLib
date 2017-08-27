package com.ss.rlib.util.ref;

import static java.lang.ThreadLocal.withInitial;

import com.ss.rlib.util.pools.PoolFactory;
import com.ss.rlib.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The enum with types of reference.
 *
 * @author JavaSaBr
 */
public enum ReferenceType {
    /**
     * Float reference type.
     */
    FLOAT(FloatReference::new, TLFloatReference::new),
    /**
     * Double reference type.
     */
    DOUBLE(DoubleReference::new, TLDoubleReference::new),
    /**
     * Char reference type.
     */
    CHAR(CharReference::new, TLCharReference::new),
    /**
     * Object reference type.
     */
    OBJECT(ObjectReference::new, TLObjectReference::new),
    /**
     * Byte reference type.
     */
    BYTE(ByteReference::new, TLByteReference::new),
    /**
     * Short reference type.
     */
    SHORT(ShortReference::new, TLShortReference::new),
    /**
     * Long reference type.
     */
    LONG(LongReference::new, TLLongReference::new),
    /**
     * Integer reference type.
     */
    INTEGER(IntegerReference::new, TLIntegerReference::new);

    /**
     * Thread local pool with references.
     */
    private final ThreadLocal<ReusablePool<Reference>> threadLocalPool = withInitial(() -> PoolFactory.newReusablePool(Reference.class));

    /**
     * The pool with references.
     */
    private final ReusablePool<Reference> pool;

    /**
     * The factory of references.
     */
    private final Supplier<Reference> factory;

    /**
     * The factory of references.
     */
    private final Supplier<Reference> threadLocalFactory;

    ReferenceType(@NotNull final Supplier<Reference> factory, @NotNull final Supplier<Reference> threadLocalFactory) {
        this.factory = factory;
        this.threadLocalFactory = threadLocalFactory;
        this.pool = PoolFactory.newConcurrentAtomicARSWLockReusablePool(Reference.class);
    }

    /**
     * Put the reference to the pool.
     *
     * @param reference put the reference to pool.
     */
    protected void put(@NotNull final UnsafeReference reference) {
        pool.put(reference);
    }

    /**
     * Put the reference to thread local pool.
     *
     * @param reference put the reference to pool.
     */
    protected void putToThreadLocal(@NotNull final UnsafeReference reference) {

        if (!reference.isThreadLocal()) {
            throw new IllegalArgumentException("the reference " + reference + " is not thread local.");
        }

        threadLocalPool.get().put(reference);
    }

    /**
     * Take reference.
     *
     * @return take the reference from thread local or create new reference.
     */
    protected @NotNull Reference take() {
        return pool.take(factory);
    }

    /**
     * Take thread local reference.
     *
     * @return take the reference from thread local or create new reference.
     */
    protected @NotNull Reference takeThreadLocal() {
        return threadLocalPool.get().take(threadLocalFactory);
    }

    /**
     * Create new reference of this type.
     *
     * @return the new reference.
     */
    protected @NotNull Reference create() {
        return factory.get();
    }

    /**
     * Gets factory.
     *
     * @return the factory of references.
     */
    protected Supplier<@NotNull Reference> getFactory() {
        return factory;
    }
}
