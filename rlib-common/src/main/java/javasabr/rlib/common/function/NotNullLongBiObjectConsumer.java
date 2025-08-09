package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullLongBiObjectConsumer<S, T> extends LongBiObjectConsumer<S, T> {

  @Override
  void accept(long first, S second, T third);
}
