package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

@FunctionalInterface
public interface NotNullBiFunction<T, U, R> extends BiFunction<T, U, R> {

    @Override
    @NotNull R apply(@NotNull T first, @NotNull U second);
}
