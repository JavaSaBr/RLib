package javasabr.rlib.functions;

/**
 * Represents an operation that accepts an int and two object arguments, and returns no result.
 *
 * @param <B> the type of the first object argument
 * @param <C> the type of the second object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface IntBiObjConsumer<B, C> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the int argument
   * @param arg2 the first object argument
   * @param arg3 the second object argument
   * @since 10.0.0
   */
  void accept(int arg1, B arg2, C arg3);
}
