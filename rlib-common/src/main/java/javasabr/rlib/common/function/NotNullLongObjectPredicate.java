package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullLongObjectPredicate<T> extends LongObjectPredicate<T> {

  boolean test(long first, T second);
}
