package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

/**
 * Represents an operation that accepts two arguments and may throw an exception.
 *
 * @param <F> the type of the first argument
 * @param <S> the type of the second argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface SafeBiConsumer<F, S> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param first the first argument
   * @param second the second argument
   * @throws Exception if an error occurs
   */
  void accept(@Nullable F first, @Nullable S second) throws Exception;
}
