package javasabr.rlib.functions;

/**
 * Represents an operation that accepts an int and an object argument.
 *
 * @param <B> the object argument type
 * @since 10.0.0
 */
@FunctionalInterface
public interface IntObjConsumer<B> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 10.0.0
   */
  void accept(int arg1, B arg2);
}
