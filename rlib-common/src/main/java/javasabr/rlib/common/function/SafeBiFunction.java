package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents a function that accepts two arguments and produces a result, and may throw an exception.
 *
 * @param <F> the type of the first argument
 * @param <S> the type of the second argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeBiFunction<F, S, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param first the first argument
   * @param second the second argument
   * @return the result
   * @throws Exception if an error occurs
   */
  @Nullable R apply(@Nullable F first, @Nullable S second) throws Exception;
}
