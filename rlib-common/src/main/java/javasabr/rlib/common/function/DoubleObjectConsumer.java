package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents an operation that accepts a double and an object argument, and returns no result.
 *
 * @param <T> the type of the object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface DoubleObjectConsumer<T> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param first the double argument
   * @param second the object argument
   */
  void accept(double first, @Nullable T second);
}
