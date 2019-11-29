package com.ss.rlib.common.util.ref;

import com.ss.rlib.common.util.pools.PoolFactory;
import com.ss.rlib.common.util.pools.ReusablePool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
public final class ReferenceFactory {

    private static final ReusablePool<GlobalByteReference> GLOBAL_BYTE_REF_POOL =
        PoolFactory.newConcurrentStampedLockReusablePool(GlobalByteReference.class);
    private static final ReusablePool<GlobalShortReference> GLOBAL_SHORT_REF_POOL =
        PoolFactory.newConcurrentStampedLockReusablePool(GlobalShortReference.class);
    private static final ReusablePool<GlobalCharReference> GLOBAL_CHAR_REF_POOL =
        PoolFactory.newConcurrentStampedLockReusablePool(GlobalCharReference.class);
    private static final ReusablePool<GlobalIntReference> GLOBAL_INT_REF_POOL =
        PoolFactory.newConcurrentStampedLockReusablePool(GlobalIntReference.class);
    private static final ReusablePool<GlobalLongReference> GLOBAL_LONG_REF_POOL =
        PoolFactory.newConcurrentStampedLockReusablePool(GlobalLongReference.class);
    private static final ReusablePool<GlobalFloatReference> GLOBAL_FLOAT_REF_POOL =
        PoolFactory.newConcurrentStampedLockReusablePool(GlobalFloatReference.class);
    private static final ReusablePool<GlobalDoubleReference> GLOBAL_DOUBLE_REF_POOL =
        PoolFactory.newConcurrentStampedLockReusablePool(GlobalDoubleReference.class);
    private static final ReusablePool<GlobalObjectReference> GLOBAL_OBJ_REF_POOL =
        PoolFactory.newConcurrentStampedLockReusablePool(GlobalObjectReference.class);

    private static final ThreadLocal<ReusablePool<TLByteReference>> TL_BYTE_REF_POOL =
        ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(TLByteReference.class));
    private static final ThreadLocal<ReusablePool<TLShortReference>> TL_SHORT_REF_POOL =
        ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(TLShortReference.class));
    private static final ThreadLocal<ReusablePool<TLCharReference>> TL_CHAR_REF_POOL =
        ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(TLCharReference.class));
    private static final ThreadLocal<ReusablePool<TLIntReference>> TL_INT_REF_POOL =
        ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(TLIntReference.class));
    private static final ThreadLocal<ReusablePool<TLLongReference>> TL_LONG_REF_POOL =
        ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(TLLongReference.class));
    private static final ThreadLocal<ReusablePool<TLFloatReference>> TL_FLOAT_REF_POOL =
        ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(TLFloatReference.class));
    private static final ThreadLocal<ReusablePool<TLDoubleReference>> TL_DOUBLE_REF_POOL =
        ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(TLDoubleReference.class));
    private static final ThreadLocal<ReusablePool<TLObjectReference>> TL_OBJECT_REF_POOL =
        ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(TLObjectReference.class));

    /**
     * @param value the init value.
     * @return the new byte ref.
     * @since 9.2.1
     */
    public static @NotNull ByteReference newByteRef(byte value) {
        return new ByteReference(value);
    }

    /**
     * @param value the init value.
     * @return the new byte ref.
     * @since 9.2.1
     */
    public static @NotNull ByteReference newReusableByteRef(byte value) {
        var ref = GLOBAL_BYTE_REF_POOL.take(GlobalByteReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new byte ref.
     * @since 9.2.1
     */
    public static @NotNull ByteReference newThreadLocalByteRef(byte value) {
        var ref = TL_BYTE_REF_POOL.get().take(TLByteReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new short ref.
     * @since 9.2.1
     */
    public static @NotNull ShortReference newShortRef(short value) {
        return new ShortReference(value);
    }

    /**
     * @param value the init value.
     * @return the new short ref.
     * @since 9.2.1
     */
    public static @NotNull ShortReference newReusableShortRef(short value) {
        var ref = GLOBAL_SHORT_REF_POOL.take(GlobalShortReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new short ref.
     * @since 9.2.1
     */
    public static @NotNull ShortReference newThreadLocalShortRef(short value) {
        var ref = TL_SHORT_REF_POOL.get().take(TLShortReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new char ref.
     * @since 9.2.1
     */
    public static @NotNull CharReference newCharRef(char value) {
        return new CharReference(value);
    }

    /**
     * @param value the init value.
     * @return the new char ref.
     * @since 9.2.1
     */
    public static @NotNull CharReference newReusableCharRef(char value) {
        var ref = GLOBAL_CHAR_REF_POOL.take(GlobalCharReference::new);
        ref.setValue(value);
        return ref ;
    }

    /**
     * @param value the init value.
     * @return the new char ref.
     * @since 9.2.1
     */
    public static @NotNull CharReference newThreadLocalCharRef(char value) {
        var ref = TL_CHAR_REF_POOL.get().take(TLCharReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new int ref.
     * @since 9.2.1
     */
    public static @NotNull IntReference newIntRef(int value) {
        return new IntReference(value);
    }

    /**
     * @param value the init value.
     * @return the new int ref.
     * @since 9.2.1
     */
    public static @NotNull IntReference newReusableIntRef(int value) {
        var ref = GLOBAL_INT_REF_POOL.take(GlobalIntReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new int ref.
     * @since 9.2.1
     */
    public static @NotNull IntReference newThreadLocalIntRef(int value) {
        var ref = TL_INT_REF_POOL.get().take(TLIntReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new long ref.
     * @since 9.2.1
     */
    public static @NotNull LongReference newLongRef(long value) {
        return new LongReference(value);
    }

    /**
     * @param value the init value.
     * @return the new long ref.
     * @since 9.2.1
     */
    public static @NotNull LongReference newReusableLongRef(long value) {
        var ref = GLOBAL_LONG_REF_POOL.take(GlobalLongReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new long ref.
     * @since 9.2.1
     */
    public static @NotNull LongReference newThreadLocalLongRef(long value) {
        var ref = TL_LONG_REF_POOL.get().take(TLLongReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new float ref.
     * @since 9.2.1
     */
    public static @NotNull FloatReference newFloatRef(float value) {
        return new FloatReference(value);
    }

    /**
     * @param value the init value.
     * @return the new float ref.
     * @since 9.2.1
     */
    public static @NotNull FloatReference newReusableFloatRef(float value) {
        var ref = GLOBAL_FLOAT_REF_POOL.take(GlobalFloatReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new float ref.
     * @since 9.2.1
     */
    public static @NotNull FloatReference newThreadLocalFloatRef(float value) {
        var ref = TL_FLOAT_REF_POOL.get().take(TLFloatReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new double ref.
     * @since 9.2.1
     */
    public static @NotNull DoubleReference newDoubleRef(double value) {
        return new DoubleReference(value);
    }

    /**
     * @param value the init value.
     * @return the new double ref.
     * @since 9.2.1
     */
    public static @NotNull DoubleReference newReusableDoubleRef(double value) {
        var ref = GLOBAL_DOUBLE_REF_POOL.take(GlobalDoubleReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @return the new double ref.
     * @since 9.2.1
     */
    public static @NotNull DoubleReference newThreadLocalDoubleRef(double value) {
        var ref = TL_DOUBLE_REF_POOL.get().take(TLDoubleReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @param <T>   the object's type.
     * @return the new object ref.
     * @since 9.2.1
     */
    public static <T> @NotNull ObjectReference<T> newObjRef(@Nullable T value) {
        return new ObjectReference<>(value);
    }

    /**
     * @param value the init value.
     * @param <T>   the object's type.
     * @return the new object ref.
     * @since 9.2.1
     */
    public static <T> @NotNull ObjectReference<T> newReusableObjRef(@Nullable T value) {
        ObjectReference<T> ref = GLOBAL_OBJ_REF_POOL.take(GlobalObjectReference::new);
        ref.setValue(value);
        return ref;
    }

    /**
     * @param value the init value.
     * @param <T>   the object's type.
     * @return the new object ref.
     * @since 9.2.1
     */
    public static <T> @NotNull ObjectReference<T> newThreadLocalObjRef(@Nullable T value) {
        TLObjectReference<T> ref = TL_OBJECT_REF_POOL.get().take(TLObjectReference::new);
        ref.setValue(value);
        return ref;
    }

    static void release(@NotNull GlobalByteReference ref) {
        GLOBAL_BYTE_REF_POOL.put(ref);
    }

    static void release(@NotNull GlobalShortReference ref) {
        GLOBAL_SHORT_REF_POOL.put(ref);
    }

    static void release(@NotNull GlobalCharReference ref) {
        GLOBAL_CHAR_REF_POOL.put(ref);
    }

    static void release(@NotNull GlobalIntReference ref) {
        GLOBAL_INT_REF_POOL.put(ref);
    }

    static void release(@NotNull GlobalLongReference ref) {
        GLOBAL_LONG_REF_POOL.put(ref);
    }

    static void release(@NotNull GlobalFloatReference ref) {
        GLOBAL_FLOAT_REF_POOL.put(ref);
    }

    static void release(@NotNull GlobalDoubleReference ref) {
        GLOBAL_DOUBLE_REF_POOL.put(ref);
    }

    static void release(@NotNull GlobalObjectReference<?> ref) {
        GLOBAL_OBJ_REF_POOL.put(ref);
    }

    static void release(@NotNull TLByteReference ref) {
        TL_BYTE_REF_POOL.get().put(ref);
    }

    static void release(@NotNull TLShortReference ref) {
        TL_SHORT_REF_POOL.get().put(ref);
    }

    static void release(@NotNull TLCharReference ref) {
        TL_CHAR_REF_POOL.get().put(ref);
    }

    static void release(@NotNull TLIntReference ref) {
        TL_INT_REF_POOL.get().put(ref);
    }

    static void release(@NotNull TLLongReference ref) {
        TL_LONG_REF_POOL.get().put(ref);
    }

    static void release(@NotNull TLFloatReference ref) {
        TL_FLOAT_REF_POOL.get().put(ref);
    }

    static void release(@NotNull TLDoubleReference ref) {
        TL_DOUBLE_REF_POOL.get().put(ref);
    }

    static void release(@NotNull TLObjectReference<?> ref) {
        TL_OBJECT_REF_POOL.get().put(ref);
    }

    private ReferenceFactory() {
        throw new IllegalArgumentException();
    }
}
