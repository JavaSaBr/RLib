package javasabr.rlib.common.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface NullableSupplier<T> extends Supplier<T> {

  @Override
  @Nullable T get();
}
