package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullSafeFactory<R> extends SafeFactory<R> {

  @Override
  R get() throws Exception;
}
