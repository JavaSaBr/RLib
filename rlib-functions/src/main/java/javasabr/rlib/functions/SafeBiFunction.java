package javasabr.rlib.functions;

/**
 * Represents a function that accepts two arguments and produces a result, and may throw an exception.
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeBiFunction<A, B, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @return the result
   * @throws Exception if an error occurs
   */
  R apply(A arg1, B arg2) throws Exception;
}
