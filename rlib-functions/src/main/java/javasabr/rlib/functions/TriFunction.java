package javasabr.rlib.functions;

/**
 * Represents a function that accepts three arguments and produces a result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the third argument type
 * @param <R> the result type
 * @since 10.0.0
 */
@FunctionalInterface
public interface TriFunction<A, B, C, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @param arg3 the third argument
   * @return the function result
   * @since 10.0.0
   */
  R apply(A arg1, B arg2, C arg3);
}
