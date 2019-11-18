package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@FunctionalInterface
public interface NotNullFunction<T, R> extends Function<T, R> {

    @Override
    @NotNull R apply(@NotNull T object);
}
