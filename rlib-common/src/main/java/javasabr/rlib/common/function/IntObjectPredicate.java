package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents a predicate that accepts an int and an object argument.
 *
 * @param <T> the type of the object argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface IntObjectPredicate<T> {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param first the int argument
   * @param second the object argument
   * @return true if the arguments match the predicate
   */
  boolean test(int first, @Nullable T second);
}
