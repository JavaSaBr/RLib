package javasabr.rlib.common.function;

@FunctionalInterface
public interface NotNullObjectLongPredicate<T> extends ObjectLongPredicate<T> {

  @Override
  boolean test(T first, long second);
}
