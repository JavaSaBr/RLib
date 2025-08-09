package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullIntBiObjectConsumer<S, T> extends IntBiObjectConsumer<S, T> {

  @Override
  void accept(int first, S second, T third);
}
