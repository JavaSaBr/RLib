package javasabr.rlib.common.function;

import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface NullableSupplier<T> extends Supplier<T> {

  @Override
  @Nullable T get();
}
