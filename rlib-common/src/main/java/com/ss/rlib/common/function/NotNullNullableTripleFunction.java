package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface NotNullNullableTripleFunction<F, S, T, R> extends TripleFunction<F, S, T, R> {

    @Override
    @Nullable R apply(@NotNull F first, @NotNull S second, @NotNull T third);
}
