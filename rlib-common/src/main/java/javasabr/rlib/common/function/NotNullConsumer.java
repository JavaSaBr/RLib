package javasabr.rlib.common.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface NotNullConsumer<T> extends Consumer<T> {

  @Override
  void accept(T object);
}
