package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullSafeConsumer<T> extends SafeConsumer<T> {

  @Override
  void accept(T argument) throws Exception;
}
