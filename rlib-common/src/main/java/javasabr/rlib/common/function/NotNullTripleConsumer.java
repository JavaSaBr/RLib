package javasabr.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullTripleConsumer<F, S, T> extends TripleConsumer<F, S, T> {

  @Override
  void accept(F first, S second, T third);
}
