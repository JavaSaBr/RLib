package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

@FunctionalInterface
public interface NotNullNullableBiFunction<T, U, R> extends BiFunction<T, U, R> {

    @Override
    @Nullable R apply(@NotNull T first, @NotNull U second);
}
