package javasabr.rlib.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

@FunctionalInterface
public interface NotNullIntFunction<R> extends IntFunction<R> {

    @Override
    @NotNull R apply(int value);
}
