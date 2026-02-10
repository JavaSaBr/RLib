package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents a supplier of results that may throw an exception.
 *
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeFactory<R> {

  /**
   * Gets a result.
   *
   * @return a result, possibly null
   * @throws Exception if an error occurs
   */
  @Nullable R get() throws Exception;
}
