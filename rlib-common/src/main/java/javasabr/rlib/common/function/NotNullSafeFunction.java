package javasabr.rlib.common.function;

@FunctionalInterface
public interface NotNullSafeFunction<F, R> extends SafeFunction<F, R> {

  @Override
  R apply(F first) throws Exception;
}
