package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullSafeTriFunction<F, S, T, R> extends SafeTriFunction<F, S, T, R> {

    @Override
    @NotNull R apply(@NotNull F first, @NotNull S second, @NotNull T third) throws Exception;
}
