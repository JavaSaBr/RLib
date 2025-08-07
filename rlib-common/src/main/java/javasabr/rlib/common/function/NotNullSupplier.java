package javasabr.rlib.common.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullSupplier<T> extends Supplier<T> {

  @Override
  @NotNull T get();
}
