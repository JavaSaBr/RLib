package javasabr.rlib.common.util;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Utility methods for null-checking and hashing objects.
 *
 * @since 9.0.2
 */
@NullMarked
public final class ObjectUtils {

  /**
   * @param <T> the object's type.
   * @param obj the object to check on not null.
   * @param message the message for exception if the object is null.
   * @return the passed object if it is not null.
   * @see Objects#requireNonNull(Object, String)
   */
  public static <T> T notNull(@Nullable T obj, String message) {
    return Objects.requireNonNull(obj, message);
  }

  /**
   * @param <T> the object's type.
   * @param obj the object to check on not null.
   * @return the passed object if it is not null.
   * @see Objects#requireNonNull(Object)
   */
  public static <T> T notNull(@Nullable T obj) {
    return Objects.requireNonNull(obj);
  }

  /**
   * Check the object to be not null. If the object is null this method throws an exception from the supplier.
   *
   * @param <T> the object's type.
   * @param obj the checked object.
   * @param supplier the exception factory.
   * @return the object.
   * @since 9.0.2
   */
  public static <T> T notNull(@Nullable T obj, Supplier<? extends RuntimeException> supplier) {
    if (obj == null) {
      throw supplier.get();
    }
    return obj;
  }

  /**
   * Check the object to be not null. If the object is null this method throws an exception from the factory.
   *
   * @param <T> the object's type.
   * @param <F> the argument's type.
   * @param obj the checked object.
   * @param arg the argument for the exception factory.
   * @param factory the exception factory.
   * @return the object.
   * @since 9.0.2
   */
  public static <T, F> T notNull(
      @Nullable T obj,
      F arg,
      Function<F, ? extends RuntimeException> factory) {
    if (obj == null) {
      throw factory.apply(arg);
    }
    return obj;
  }

  /**
   * Check the object to be not null. If the object is null this method throws an exception from the factory.
   *
   * @param <T> the object's type.
   * @param obj the checked object.
   * @param arg the argument for the exception factory.
   * @param factory the exception factory.
   * @return the object.
   * @since 9.0.3
   */
  public static <T> T notNull(
      @Nullable T obj,
      long arg,
      LongFunction<? extends RuntimeException> factory) {
    if (obj == null) {
      throw factory.apply(arg);
    }
    return obj;
  }

  /**
   * Returns the another object if the first object is null.
   *
   * @param obj the object.
   * @param another the another object.
   * @param <T> the object's type.
   * @return the another object if the first object is null.
   */
  public static <T> T ifNull(@Nullable T obj, T another) {
    return obj == null ? another : obj;
  }

  /**
   * Returns a new object if the first object is null.
   *
   * @param obj the object.
   * @param factory the factory.
   * @param <T> the object's type.
   * @return a new object if the first object is null.
   */
  public static <T> T ifNull(@Nullable T obj, Supplier<T> factory) {
    return obj == null ? factory.get() : obj;
  }
  
  @Deprecated(forRemoval = true)
  public static int hash(boolean value) {
    return Boolean.hashCode(value);
  }
  
  @Deprecated(forRemoval = true)
  public static int hash(long value) {
    return Long.hashCode(value);
  }

  /**
   * Gets hash of the object.
   *
   * @param object the object.
   * @return the hash.
   */
  public static int hash(@Nullable Object object) {
    return object == null ? 0 : object.hashCode();
  }
}
