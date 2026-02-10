package javasabr.rlib.common.function;

/**
 * Represents a function that accepts two non-null arguments and produces a non-null result,
 * and may throw an exception.
 *
 * @param <F> the type of the first argument
 * @param <S> the type of the second argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface NotNullSafeBiFunction<F, S, R> extends SafeBiFunction<F, S, R> {

  @Override
  R apply(F first, S second) throws Exception;
}
