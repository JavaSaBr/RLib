package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullSafeBiConsumer<F, S> extends SafeBiConsumer<F, S> {

  @Override
  void accept(F first, S second) throws Exception;
}
