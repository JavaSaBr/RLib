package javasabr.rlib.functions;

/**
 * Represents an operation that accepts a single argument and may throw an exception.
 *
 * @param <A> the type of the input argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeConsumer<A> {

  /**
   * Performs this operation on the given argument.
   *
   * @param arg the input argument
   * @throws Exception if an error occurs
   */
  void accept(A arg) throws Exception;
}
