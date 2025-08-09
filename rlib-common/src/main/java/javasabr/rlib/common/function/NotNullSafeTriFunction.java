package javasabr.rlib.common.function;

@FunctionalInterface
public interface NotNullSafeTriFunction<F, S, T, R> extends SafeTriFunction<F, S, T, R> {

  @Override
  R apply(F first, S second, T third) throws Exception;
}
