package javasabr.rlib.common.function;

import java.util.function.BiConsumer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullBiConsumer<T, U> extends BiConsumer<T, U> {

  @Override
  void accept(@NotNull T first, @NotNull U second);
}
