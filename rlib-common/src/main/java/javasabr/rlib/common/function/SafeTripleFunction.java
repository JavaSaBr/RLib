package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

/**
 * The function.
 *
 * @param <F> the type parameter
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @param <R> the type parameter
 * @author JavaSaBr
 */
@NullUnmarked
@FunctionalInterface
public interface SafeTripleFunction<F, S, T, R> {

  /**
   * Apply r.
   *
   * @param first the first
   * @param second the second
   * @param third the third
   * @return the r
   * @throws Exception the exception
   */
  R apply(F first, S second, T third) throws Exception;
}
