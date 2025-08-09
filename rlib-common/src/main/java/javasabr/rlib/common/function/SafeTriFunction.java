package javasabr.rlib.common.function;

import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
@FunctionalInterface
public interface SafeTriFunction<F, S, T, R> {

  R apply(F first, S second, T third) throws Exception;
}
