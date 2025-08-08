package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullFloatBiObjectConsumer<S, T> extends FloatBiObjectConsumer<S, T> {

  @Override
  void accept(float first, S second, T third);
}
