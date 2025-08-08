package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullIntObjectPredicate<T> extends IntObjectPredicate<T> {

  boolean test(int first, T second);
}
