package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@FunctionalInterface
public interface NotNullNullableFunction<T, R> extends Function<T, R> {

    @Override
    @Nullable R apply(@NotNull T object);
}
