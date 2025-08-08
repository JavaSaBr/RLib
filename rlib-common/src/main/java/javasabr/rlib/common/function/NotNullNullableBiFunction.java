package javasabr.rlib.common.function;

import java.util.function.BiFunction;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface NotNullNullableBiFunction<T, U, R> extends BiFunction<T, U, R> {

  @Override
  @Nullable R apply(T first, U second);
}
