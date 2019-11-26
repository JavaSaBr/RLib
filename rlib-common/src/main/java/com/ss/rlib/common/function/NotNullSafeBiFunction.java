package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullSafeBiFunction<F, S, R> extends SafeBiFunction<F, S, R> {

    @Override
    @NotNull R apply(@NotNull F first, @NotNull S second) throws Exception;
}
