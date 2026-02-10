package javasabr.rlib.common.function;

import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single non-null argument and returns no result.
 *
 * @param <T> the type of the input argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface NotNullConsumer<T> extends Consumer<T> {

  @Override
  void accept(T object);
}
