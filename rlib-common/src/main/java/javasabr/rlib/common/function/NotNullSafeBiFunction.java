package javasabr.rlib.common.function;

@FunctionalInterface
public interface NotNullSafeBiFunction<F, S, R> extends SafeBiFunction<F, S, R> {

  @Override
  R apply(F first, S second) throws Exception;
}
