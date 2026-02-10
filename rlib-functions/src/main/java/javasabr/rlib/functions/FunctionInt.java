package javasabr.rlib.functions;

/**
 * Represents a function that accepts an object and produces an int result.
 *
 * @param <A> the type of the input argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface FunctionInt<A> {

  /**
   * Applies this function to the given argument.
   *
   * @param arg the function argument
   * @return the int result
   */
  int apply(A arg);
}
