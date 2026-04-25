package javasabr.rlib.functions;

/**
 * Represents an operation that accepts three arguments and returns no result.
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <C> the type of the third argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @param arg3 the third argument
   * @since 10.0.0
   */
  void accept(A arg1, B arg2, C arg3);
}
