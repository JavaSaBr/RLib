package javasabr.rlib.functions;

/**
 * Represents an operation that accepts two object arguments and a long argument, and returns no result.
 *
 * @param <A> the type of the first object argument
 * @param <B> the type of the second object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface BiObjLongConsumer<A, B> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the first object argument
   * @param arg2 the second object argument
   * @param arg3 the long argument
   * @since 10.0.0
   */
  void accept(A arg1, B arg2, long arg3);
}
