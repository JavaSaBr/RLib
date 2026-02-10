package javasabr.rlib.functions;

/**
 * Represents an operation that accepts two arguments and may throw an exception.
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeBiConsumer<A, B> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @throws Exception if an error occurs
   */
  void accept(A arg1, B arg2) throws Exception;
}
