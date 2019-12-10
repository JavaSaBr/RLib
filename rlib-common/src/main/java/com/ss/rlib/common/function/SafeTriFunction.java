package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface SafeTriFunction<F, S, T, R> {

    @Nullable R apply(@Nullable F first, @Nullable S second, @Nullable T third) throws Exception;
}
