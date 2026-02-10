package javasabr.rlib.common.function;

/**
 * Represents a function that accepts three non-null arguments and produces a non-null result,
 * and may throw an exception.
 *
 * @param <F> the type of the first argument
 * @param <S> the type of the second argument
 * @param <T> the type of the third argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface NotNullSafeTriFunction<F, S, T, R> extends SafeTriFunction<F, S, T, R> {

  @Override
  R apply(F first, S second, T third) throws Exception;
}
