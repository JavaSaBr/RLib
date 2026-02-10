package javasabr.rlib.functions;

/**
 * Represents an operation that accepts an object and a boolean argument.
 *
 * @param <A> the object argument type
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjBoolConsumer<A> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 10.0.0
   */
  void accept(A arg1, boolean arg2);
}
