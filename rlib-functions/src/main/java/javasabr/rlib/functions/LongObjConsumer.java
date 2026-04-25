package javasabr.rlib.functions;

/**
 * Represents an operation that accepts a long and an object argument, and returns no result.
 *
 * @param <B> the type of the object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface LongObjConsumer<B> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the long argument
   * @param arg2 the object argument
   * @since 10.0.0
   */
  void accept(long arg1, B arg2);
}
