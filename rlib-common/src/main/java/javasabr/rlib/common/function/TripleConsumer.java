package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * Represents an operation that accepts three arguments and returns no result.
 *
 * @param <F> the type of the first argument
 * @param <S> the type of the second argument
 * @param <T> the type of the third argument
 * @since 10.0.0
 */
@NullUnmarked
@FunctionalInterface
public interface TripleConsumer<F, S, T> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param first the first argument
   * @param second the second argument
   * @param third the third argument
   */
  void accept(F first, S second, T third);
}
