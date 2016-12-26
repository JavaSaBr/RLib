package rlib.util.ref;

import static java.lang.ThreadLocal.withInitial;
import static rlib.util.pools.PoolFactory.newReusablePool;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import rlib.util.pools.PoolFactory;
import rlib.util.pools.ReusablePool;

/**
 * The enum with types of reference.
 *
 * @author JavaSaBr
 */
public enum ReferenceType {
    FLOAT(FloatReference::new, TLFloatReference::new),
    DOUBLE(DoubleReference::new, TLDoubleReference::new),
    CHAR(CharReference::new, TLCharReference::new),
    OBJECT(ObjectReference::new, TLObjectReference::new),
    BYTE(ByteReference::new, TLByteReference::new),
    SHORT(ShortReference::new, TLShortReference::new),
    LONG(LongReference::new, TLLongReference::new),
    INTEGER(IntegerReference::new, TLIntegerReference::new);

    /**
     * Thread local pool with references.
     */
    private final ThreadLocal<ReusablePool<Reference>> threadLocalPool = withInitial(() -> newReusablePool(Reference.class));

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
     * @param reference put the reference to pool.
     */
    protected void put(@NotNull final UnsafeReference reference) {
        pool.put(reference);
    }

    /**
     * @param reference put the reference to pool.
     */
    protected void putToThreadLocal(@NotNull final UnsafeReference reference) {
        if (!reference.isThreadLocal())
            throw new IllegalArgumentException("the reference " + reference + " is not thread local.");
        threadLocalPool.get().put(reference);
    }

    /**
     * @return take the reference from thread local or create new reference.
     */
    @NotNull
    protected Reference take() {
        return pool.take(factory);
    }

    /**
     * @return take the reference from thread local or create new reference.
     */
    @NotNull
    protected Reference takeThreadLocal() {
        return threadLocalPool.get().take(threadLocalFactory);
    }

    /**
     * Create new reference of this type.
     *
     * @return the new reference.
     */
    @NotNull
    protected Reference create() {
        return factory.get();
    }

    /**
     * @return the factory of references.
     */
    protected Supplier<Reference> getFactory() {
        return factory;
    }
}
