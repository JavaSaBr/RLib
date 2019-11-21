package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullFunctionInt<T> extends FunctionInt<T> {

    int apply(@NotNull T object);
}
