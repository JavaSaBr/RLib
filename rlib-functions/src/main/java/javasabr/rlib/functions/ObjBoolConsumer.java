package javasabr.rlib.functions;

/**
 * Represents an operation that accepts an object and a boolean argument, and returns no result.
 *
 * @param <A> the type of the object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjBoolConsumer<A> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the object argument
   * @param arg2 the boolean argument
   * @since 10.0.0
   */
  void accept(A arg1, boolean arg2);
}
