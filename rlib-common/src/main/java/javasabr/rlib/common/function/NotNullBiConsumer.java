package javasabr.rlib.common.function;

import java.util.function.BiConsumer;

/**
 * Represents an operation that accepts two non-null arguments and returns no result.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @since 10.0.0
 */
@FunctionalInterface
public interface NotNullBiConsumer<T, U> extends BiConsumer<T, U> {

  @Override
  void accept(T first, U second);
}
