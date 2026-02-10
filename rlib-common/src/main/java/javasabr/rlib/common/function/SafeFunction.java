package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents a function that accepts one argument and produces a result, and may throw an exception.
 *
 * @param <F> the type of the input argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeFunction<F, R> {

  /**
   * Applies this function to the given argument.
   *
   * @param first the function argument
   * @return the result
   * @throws Exception if an error occurs
   */
  @Nullable R apply(@Nullable F first) throws Exception;
}
