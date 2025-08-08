package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * The function.
 *
 * @param <F> the type parameter
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@NullUnmarked
@FunctionalInterface
public interface TriplePredicate<F, S, T> {

  /**
   * Test boolean.
   *
   * @param first the first
   * @param second the second
   * @param third the third
   * @return the boolean
   */
  boolean test(F first, S second, T third);
}
