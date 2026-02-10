package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents an operation that accepts a single argument and may throw an exception.
 *
 * @param <T> the type of the input argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeConsumer<T> {

  /**
   * Performs this operation on the given argument.
   *
   * @param argument the input argument
   * @throws Exception if an error occurs
   */
  void accept(@Nullable T argument) throws Exception;
}
