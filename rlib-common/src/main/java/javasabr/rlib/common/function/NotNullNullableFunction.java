package javasabr.rlib.common.function;

import java.util.function.Function;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface NotNullNullableFunction<T, R> extends Function<T, R> {

  @Override
  @Nullable
  R apply(T object);
}
