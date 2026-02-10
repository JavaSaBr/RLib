package javasabr.rlib.functions;

/**
 * Represents an operation that accepts an object, a long, and another object argument.
 *
 * @param <A> the first object argument type
 * @param <C> the third object argument type
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjLongObjConsumer<A, C> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @param arg3 the third argument
   * @since 10.0.0
   */
  void accept(A arg1, long arg2, C arg3);
}
