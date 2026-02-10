package javasabr.rlib.functions;

/**
 * Represents a function that accepts a byte argument and produces a result.
 *
 * @param <R> the result type
 * @since 10.0.0
 */
@FunctionalInterface
public interface ByteFunction<R> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the argument
   * @return the function result
   * @since 10.0.0
   */
  R apply(byte value);
}
