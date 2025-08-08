package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * The function.
 *
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@NullUnmarked
@FunctionalInterface
public interface IntBiObjectConsumer<S, T> {

  /**
   * Accept.
   *
   * @param first the first
   * @param second the second
   * @param third the third
   */
  void accept(int first, S second, T third);
}
