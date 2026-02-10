package javasabr.rlib.common.function;

/**
 * Represents a supplier of non-null results that may throw an exception.
 *
 * @param <R> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface NotNullSafeFactory<R> extends SafeFactory<R> {

  @Override
  R get() throws Exception;
}
