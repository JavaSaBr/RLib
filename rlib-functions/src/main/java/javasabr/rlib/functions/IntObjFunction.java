package javasabr.rlib.functions;

/**
 * Represents a function that accepts an int and an object argument and produces a result.
 *
 * @param <B> the type of the object argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface IntObjFunction<B, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param arg1 the int argument
   * @param arg2 the object argument
   * @return the function result
   * @since 10.0.0
   */
  R apply(int arg1, B arg2);
}
