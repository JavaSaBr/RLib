package javasabr.rlib.functions;

import org.jspecify.annotations.NullUnmarked;

/**
 * Represents an operation that accepts a float and two object arguments, and returns no result.
 *
 * @param <B> the type of the first object argument
 * @param <C> the type of the second object argument
 * @since 10.0.0
 */
@NullUnmarked
@FunctionalInterface
public interface FloatBiObjConsumer<B, C> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param arg1 the float argument
   * @param arg2 the first object argument
   * @param arg3 the second object argument
   * @since 10.0.0
   */
  void accept(float arg1, B arg2, C arg3);
}
