package javasabr.rlib.common.function;

import java.util.function.Predicate;

@FunctionalInterface
public interface NotNullPredicate<T> extends Predicate<T> {

  @Override
  boolean test(T object);
}
