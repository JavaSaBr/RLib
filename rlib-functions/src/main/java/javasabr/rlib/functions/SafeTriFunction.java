package javasabr.rlib.functions;

/**
 * Represents a function that accepts three arguments and produces a result, and may throw an exception.
 *
 * @param <F> the type of the first argument
 * @param <S> the type of the second argument
 * @param <T> the type of the third argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeTriFunction<F, S, T, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @param arg3 the third argument
   * @return the result
   * @throws Exception if an error occurs
   */
  R apply(F arg1, S arg2, T arg3) throws Exception;
}
