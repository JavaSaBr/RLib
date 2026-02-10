package javasabr.rlib.common.function;

/**
 * Represents an operation that accepts two non-null arguments and may throw an exception.
 *
 * @param <F> the type of the first argument
 * @param <S> the type of the second argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface NotNullSafeBiConsumer<F, S> extends SafeBiConsumer<F, S> {

  @Override
  void accept(F first, S second) throws Exception;
}
