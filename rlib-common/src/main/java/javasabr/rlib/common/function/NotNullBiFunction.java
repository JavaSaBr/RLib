package javasabr.rlib.common.function;

import java.util.function.BiFunction;

@FunctionalInterface
public interface NotNullBiFunction<T, U, R> extends BiFunction<T, U, R> {

  @Override
  R apply(T first, U second);
}
