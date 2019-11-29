package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface SafeBiFunction<F, S, R> {

    @Nullable R apply(@Nullable F first, @Nullable S second) throws Exception;
}
