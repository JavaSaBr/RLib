package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents a function that accepts an object and an int argument and produces a result.
 *
 * @param <T> the type of the object argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjectIntFunction<T, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param first the object argument
   * @param second the int argument
   * @return the result
   */
  @Nullable R apply(@Nullable T first, int second);
}
