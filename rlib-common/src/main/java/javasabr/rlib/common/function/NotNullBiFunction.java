package javasabr.rlib.common.function;

import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullBiFunction<T, U, R> extends BiFunction<T, U, R> {

    @Override
    @NotNull R apply(@NotNull T first, @NotNull U second);
}
