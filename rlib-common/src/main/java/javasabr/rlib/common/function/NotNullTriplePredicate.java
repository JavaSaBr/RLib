package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullTriplePredicate<F, S, T> extends TriplePredicate<F, S, T> {

  boolean test(F first, S second, T third);
}
