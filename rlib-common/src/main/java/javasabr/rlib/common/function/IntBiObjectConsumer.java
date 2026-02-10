package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * Represents an operation that accepts an int and two object arguments, and returns no result.
 *
 * @param <S> the type of the first object argument
 * @param <T> the type of the second object argument
 * @since 10.0.0
 */
@NullUnmarked
@FunctionalInterface
public interface IntBiObjectConsumer<S, T> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param first the int argument
   * @param second the first object argument
   * @param third the second object argument
   */
  void accept(int first, S second, T third);
}
