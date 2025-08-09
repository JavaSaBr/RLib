package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * The consumer with 5 arguments.
 *
 * @param <F> the type parameter
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @param <FO> the type parameter
 * @param <FI> the type parameter
 * @param <SX> the type parameter
 * @author JavaSaBr
 */
@NullUnmarked
@FunctionalInterface
public interface SixObjectConsumer<F, S, T, FO, FI, SX> {

  /**
   * Accept.
   *
   * @param first the first
   * @param second the second
   * @param third the third
   * @param fourth the fourth
   * @param five the five
   * @param six the six
   */
  void accept(F first, S second, T third, FO fourth, FI five, SX six);
}
