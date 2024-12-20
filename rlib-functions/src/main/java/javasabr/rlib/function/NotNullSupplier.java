package javasabr.rlib.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface NotNullSupplier<T> extends Supplier<T> {

    @Override
    @NotNull T get();
}
