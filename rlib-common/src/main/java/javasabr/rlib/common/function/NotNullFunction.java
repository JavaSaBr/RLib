package javasabr.rlib.common.function;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullFunction<T, R> extends Function<T, R> {

  @Override
  @NotNull R apply(@NotNull T object);
}
