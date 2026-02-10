package javasabr.rlib.functions;

/**
 * Represents a function that accepts an object and a long argument and produces a result.
 *
 * @param <A> the object argument type
 * @param <R> the result type
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjLongFunction<A, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @return the function result
   * @since 10.0.0
   */
  R apply(A arg1, long arg2);
}
