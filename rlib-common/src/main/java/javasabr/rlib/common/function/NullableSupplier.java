package javasabr.rlib.common.function;

import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

/**
 * Represents a supplier of results that may return null.
 *
 * @param <T> the type of the result
 * @since 10.0.0
 */
@FunctionalInterface
public interface NullableSupplier<T> extends Supplier<T> {

  @Override
  @Nullable T get();
}
