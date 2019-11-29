package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullSafeFunction<F, R> extends SafeFunction<F, R> {

    @Override
    @NotNull R apply(@NotNull F first) throws Exception;
}
