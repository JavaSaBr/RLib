package javasabr.rlib.functions;

/**
 * Represents a function that accepts two object arguments and produces a boolean result.
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface BiObjToBoolFunction<A, B> {

  /**
   * Applies this function to the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @return true or false based on the evaluation
   * @since 10.0.0
   */
  boolean apply(A arg1, B arg2);
}
