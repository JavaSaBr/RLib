package javasabr.rlib.functions;

/**
 * Represents an operation that accepts a single float argument and returns no result.
 *
 * @since 10.0.0
 */
@FunctionalInterface
public interface FloatConsumer {

  /**
   * Performs this operation on the given float value.
   *
   * @param arg the float value
   */
  void consume(float arg);
}
