package javasabr.rlib.functions;

/**
 * Represents an operation that accepts an object, a long, and another object argument, and returns no result.
 *
 * @param <A> the type of the first object argument
 * @param <C> the type of the second object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjLongObjConsumer<A, C> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the first object argument
   * @param arg2 the long argument
   * @param arg3 the second object argument
   * @since 10.0.0
   */
  void accept(A arg1, long arg2, C arg3);
}
