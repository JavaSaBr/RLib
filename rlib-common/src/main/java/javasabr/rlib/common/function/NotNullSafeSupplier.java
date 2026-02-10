package javasabr.rlib.common.function;

/**
 * Represents a supplier of non-null results that may throw an exception.
 *
 * @param <T> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface NotNullSafeSupplier<T> extends SafeSupplier<T> {

  @Override
  T get() throws Exception;
}
