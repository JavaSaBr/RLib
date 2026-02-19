package javasabr.rlib.reference;

import javasabr.rlib.reusable.pool.PoolFactory;
import javasabr.rlib.reusable.pool.ReusablePool;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.Nullable;

/**
 * Factory for creating reference instances, including thread-local pooled references.
 *
 * @since 10.0.0
 */
@UtilityClass
public final class ReferenceFactory {

  private static final ThreadLocal<ReusablePool<TLByteReference>> TL_BYTE_REF_POOL =
      ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(
      TLByteReference.class));
  private static final ThreadLocal<ReusablePool<TLShortReference>> TL_SHORT_REF_POOL =
      ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(
      TLShortReference.class));
  private static final ThreadLocal<ReusablePool<TLCharReference>> TL_CHAR_REF_POOL =
      ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(
      TLCharReference.class));
  private static final ThreadLocal<ReusablePool<TLIntReference>> TL_INT_REF_POOL =
      ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(
      TLIntReference.class));
  private static final ThreadLocal<ReusablePool<TLLongReference>> TL_LONG_REF_POOL =
      ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(
      TLLongReference.class));
  private static final ThreadLocal<ReusablePool<TLFloatReference>> TL_FLOAT_REF_POOL =
      ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(
      TLFloatReference.class));
  private static final ThreadLocal<ReusablePool<TLDoubleReference>> TL_DOUBLE_REF_POOL =
      ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(
      TLDoubleReference.class));
  private static final ThreadLocal<ReusablePool<TLObjectReference>> TL_OBJECT_REF_POOL =
      ThreadLocal.withInitial(() -> PoolFactory.newReusablePool(
      TLObjectReference.class));

  /**
   * Creates a new byte reference with the specified value.
   *
   * @param value the initial value
   * @return a new byte reference
   * @since 10.0.0
   */
  public static ByteReference byteRef(byte value) {
    return new ByteReference(value);
  }

  /**
   * Returns a thread-local pooled byte reference with the specified value.
   *
   * @param value the initial value
   * @return a reusable byte reference from the thread-local pool
   * @since 10.0.0
   */
  public static TLByteReference threadLocalByteRef(byte value) {
    var ref = TL_BYTE_REF_POOL.get().take(TLByteReference::new);
    ref.value(value);
    return ref;
  }

  /**
   * Creates a new short reference with the specified value.
   *
   * @param value the initial value
   * @return a new short reference
   * @since 10.0.0
   */
  public static ShortReference newShortRef(short value) {
    return new ShortReference(value);
  }

  /**
   * Returns a thread-local pooled short reference with the specified value.
   *
   * @param value the initial value
   * @return a reusable short reference from the thread-local pool
   * @since 10.0.0
   */
  public static TLShortReference threadLocalShortRef(short value) {
    var ref = TL_SHORT_REF_POOL.get().take(TLShortReference::new);
    ref.value(value);
    return ref;
  }

  /**
   * Creates a new char reference with the specified value.
   *
   * @param value the initial value
   * @return a new char reference
   * @since 10.0.0
   */
  public static CharReference charRef(char value) {
    return new CharReference(value);
  }

  /**
   * Returns a thread-local pooled char reference with the specified value.
   *
   * @param value the initial value
   * @return a reusable char reference from the thread-local pool
   * @since 10.0.0
   */
  public static TLCharReference threadLocalCharRef(char value) {
    var ref = TL_CHAR_REF_POOL.get().take(TLCharReference::new);
    ref.value(value);
    return ref;
  }

  /**
   * Creates a new int reference with the specified value.
   *
   * @param value the initial value
   * @return a new int reference
   * @since 10.0.0
   */
  public static IntReference intRef(int value) {
    return new IntReference(value);
  }

  /**
   * Returns a thread-local pooled int reference with the specified value.
   *
   * @param value the initial value
   * @return a reusable int reference from the thread-local pool
   * @since 10.0.0
   */
  public static TLIntReference threadLocalIntRef(int value) {
    var ref = TL_INT_REF_POOL.get().take(TLIntReference::new);
    ref.value(value);
    return ref;
  }

  /**
   * Creates a new long reference with the specified value.
   *
   * @param value the initial value
   * @return a new long reference
   * @since 10.0.0
   */
  public static LongReference longRef(long value) {
    return new LongReference(value);
  }

  /**
   * Returns a thread-local pooled long reference with the specified value.
   *
   * @param value the initial value
   * @return a reusable long reference from the thread-local pool
   * @since 10.0.0
   */
  public static TLLongReference threadLocalLongRef(long value) {
    var ref = TL_LONG_REF_POOL.get().take(TLLongReference::new);
    ref.value(value);
    return ref;
  }

  /**
   * Creates a new float reference with the specified value.
   *
   * @param value the initial value
   * @return a new float reference
   * @since 10.0.0
   */
  public static FloatReference floatRef(float value) {
    return new FloatReference(value);
  }

  /**
   * Returns a thread-local pooled float reference with the specified value.
   *
   * @param value the initial value
   * @return a reusable float reference from the thread-local pool
   * @since 10.0.0
   */
  public static TLFloatReference threadLocalFloatRef(float value) {
    var ref = TL_FLOAT_REF_POOL.get().take(TLFloatReference::new);
    ref.value(value);
    return ref;
  }

  /**
   * Creates a new double reference with the specified value.
   *
   * @param value the initial value
   * @return a new double reference
   * @since 10.0.0
   */
  public static DoubleReference doubleRef(double value) {
    return new DoubleReference(value);
  }

  /**
   * Returns a thread-local pooled double reference with the specified value.
   *
   * @param value the initial value
   * @return a reusable double reference from the thread-local pool
   * @since 10.0.0
   */
  public static TLDoubleReference threadLocalDoubleRef(double value) {
    var ref = TL_DOUBLE_REF_POOL.get().take(TLDoubleReference::new);
    ref.value(value);
    return ref;
  }

  /**
   * Creates a new object reference with the specified value.
   *
   * @param <T> the type of the referenced object
   * @param value the initial value, or null
   * @return a new object reference
   * @since 10.0.0
   */
  public static <T> ObjectReference<T> objRef(@Nullable T value) {
    return new ObjectReference<>(value);
  }

  /**
   * Returns a thread-local pooled object reference with the specified value.
   *
   * @param <T> the type of the referenced object
   * @param value the initial value, or null
   * @return a reusable object reference from the thread-local pool
   * @since 10.0.0
   */
  public static <T> TLObjectReference<T> threadLocalObjRef(@Nullable T value) {
    @SuppressWarnings("unchecked")
    TLObjectReference<T> ref = TL_OBJECT_REF_POOL.get().take(TLObjectReference::new);
    ref.value(value);
    return ref;
  }

  static void release(TLByteReference ref) {
    TL_BYTE_REF_POOL.get().put(ref);
  }

  static void release(TLShortReference ref) {
    TL_SHORT_REF_POOL.get().put(ref);
  }

  static void release(TLCharReference ref) {
    TL_CHAR_REF_POOL.get().put(ref);
  }

  static void release(TLIntReference ref) {
    TL_INT_REF_POOL.get().put(ref);
  }

  static void release(TLLongReference ref) {
    TL_LONG_REF_POOL.get().put(ref);
  }

  static void release(TLFloatReference ref) {
    TL_FLOAT_REF_POOL.get().put(ref);
  }

  static void release(TLDoubleReference ref) {
    TL_DOUBLE_REF_POOL.get().put(ref);
  }

  static void release(TLObjectReference<?> ref) {
    TL_OBJECT_REF_POOL.get().put(ref);
  }
}
