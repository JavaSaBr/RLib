package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * Represents an operation that accepts a float and two object arguments, and returns no result.
 *
 * @param <S> the type of the first object argument
 * @param <T> the type of the second object argument
 * @since 10.0.0
 */
@NullUnmarked
@FunctionalInterface
public interface FloatBiObjectConsumer<S, T> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param first the float argument
   * @param second the first object argument
   * @param third the second object argument
   */
  void accept(float first, S second, T third);
}
