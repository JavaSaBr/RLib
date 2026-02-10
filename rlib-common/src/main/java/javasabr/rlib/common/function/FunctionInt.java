package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents a function that accepts an object and produces an int result.
 *
 * @param <T> the type of the input argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface FunctionInt<T> {

  /**
   * Applies this function to the given argument.
   *
   * @param first the function argument
   * @return the int result
   */
  int apply(@Nullable T first);
}
