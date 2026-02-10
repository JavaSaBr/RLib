package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * Represents a predicate that accepts three arguments.
 *
 * @param <F> the type of the first argument
 * @param <S> the type of the second argument
 * @param <T> the type of the third argument
 * @since 10.0.0
 */
@NullUnmarked
@FunctionalInterface
public interface TriplePredicate<F, S, T> {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param first the first argument
   * @param second the second argument
   * @param third the third argument
   * @return true if the arguments match the predicate
   */
  boolean test(F first, S second, T third);
}
