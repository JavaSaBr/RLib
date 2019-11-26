package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface SafeFunction<F, R> {

    @Nullable R apply(@Nullable F first) throws Exception;
}
