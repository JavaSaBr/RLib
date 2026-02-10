package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * Represents a function that accepts three arguments and produces a result, and may throw an exception.
 *
 * @param <F> the type of the first argument
 * @param <S> the type of the second argument
 * @param <T> the type of the third argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@NullUnmarked
@FunctionalInterface
public interface SafeTriFunction<F, S, T, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param first the first argument
   * @param second the second argument
   * @param third the third argument
   * @return the result
   * @throws Exception if an error occurs
   */
  R apply(F first, S second, T third) throws Exception;
}
