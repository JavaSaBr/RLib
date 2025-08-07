package javasabr.rlib.common.function;

import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface NotNullNullableBiFunction<T, U, R> extends BiFunction<T, U, R> {

  @Override
  @Nullable R apply(@NotNull T first, @NotNull U second);
}
