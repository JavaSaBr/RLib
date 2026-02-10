package javasabr.rlib.functions;

/**
 * Represents an operation that accepts two object arguments and a long argument.
 *
 * @param <A> the first object argument type
 * @param <B> the second object argument type
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjObjLongConsumer<A, B> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @param arg3 the third argument
   * @since 10.0.0
   */
  void accept(A arg1, B arg2, long arg3);
}
