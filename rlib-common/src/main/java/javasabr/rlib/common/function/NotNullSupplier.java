package javasabr.rlib.common.function;

import java.util.function.Supplier;

@FunctionalInterface
public interface NotNullSupplier<T> extends Supplier<T> {

  @Override
  T get();
}
