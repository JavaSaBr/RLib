package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullSafeSupplier<T> extends SafeSupplier<T> {

  @Override
  T get() throws Exception;
}
