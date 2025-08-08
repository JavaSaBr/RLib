package javasabr.rlib.common.function;

import java.util.function.Function;

@FunctionalInterface
public interface NotNullFunction<T, R> extends Function<T, R> {

  @Override
  R apply(T object);
}
