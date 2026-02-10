package javasabr.rlib.functions;

/**
 * Represents a function that accepts one argument and produces a result, and may throw an exception.
 *
 * @param <A> the type of the input argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeFunction<A, R> {

  /**
   * Applies this function to the given argument.
   *
   * @param arg the function argument
   * @return the result
   * @throws Exception if an error occurs
   */
  R apply(A arg) throws Exception;
}
