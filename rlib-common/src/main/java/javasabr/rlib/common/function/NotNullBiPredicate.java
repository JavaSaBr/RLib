package javasabr.rlib.common.function;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface NotNullBiPredicate<T, U> extends BiPredicate<T, U> {

  @Override
  boolean test(T first, U second);
}
