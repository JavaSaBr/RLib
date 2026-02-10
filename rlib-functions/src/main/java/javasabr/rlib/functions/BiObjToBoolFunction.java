package javasabr.rlib.functions;

/**
 * Represents a function that accepts two object arguments and produces a boolean result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @since 10.0.0
 */
@FunctionalInterface
public interface BiObjToBoolFunction<A, B> {

  /**
   * Applies this function to the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @return the function result
   * @since 10.0.0
   */
  boolean apply(A arg1, B arg2);
}
