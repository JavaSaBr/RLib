package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullObjectIntPredicate<T> extends ObjectIntPredicate<T> {

  @Override
  boolean test(T first, int second);
}
