package javasabr.rlib.common.function;

/**
 * Represents a function that accepts a non-null argument and produces a non-null result,
 * and may throw an exception.
 *
 * @param <F> the type of the input argument
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface NotNullSafeFunction<F, R> extends SafeFunction<F, R> {

  @Override
  R apply(F first) throws Exception;
}
