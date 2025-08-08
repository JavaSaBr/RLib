package javasabr.rlib.common.function;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface NotNullBiConsumer<T, U> extends BiConsumer<T, U> {

  @Override
  void accept(T first, U second);
}
