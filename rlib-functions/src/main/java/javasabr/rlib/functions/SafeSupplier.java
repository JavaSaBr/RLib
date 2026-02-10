package javasabr.rlib.functions;

import org.jspecify.annotations.Nullable;

/**
 * Represents a supplier of results that may throw an exception.
 *
 * @param <T> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeSupplier<T> {

  /**
   * Gets a result.
   *
   * @return a result, possibly null
   * @throws Exception if an error occurs
   */
  @Nullable T get() throws Exception;
}
