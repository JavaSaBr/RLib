package javasabr.rlib.common.function;

import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface SafeFunction<F, R> {

  @Nullable R apply(@Nullable F first) throws Exception;
}
