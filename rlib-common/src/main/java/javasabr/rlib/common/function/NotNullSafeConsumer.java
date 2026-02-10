package javasabr.rlib.common.function;

/**
 * Represents an operation that accepts a single non-null argument and may throw an exception.
 *
 * @param <T> the type of the input argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface NotNullSafeConsumer<T> extends SafeConsumer<T> {

  @Override
  void accept(T argument) throws Exception;
}
