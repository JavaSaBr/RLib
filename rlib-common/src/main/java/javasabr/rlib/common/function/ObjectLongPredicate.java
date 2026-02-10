package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents a predicate that accepts an object and a long argument.
 *
 * @param <T> the type of the object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface ObjectLongPredicate<T> {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param first the object argument
   * @param second the long argument
   * @return true if the arguments match the predicate
   */
  boolean test(@Nullable T first, long second);
}
