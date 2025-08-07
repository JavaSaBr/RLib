package javasabr.rlib.common.function;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface NotNullNullableFunction<T, R> extends Function<T, R> {

    @Override
    @Nullable R apply(@NotNull T object);
}
