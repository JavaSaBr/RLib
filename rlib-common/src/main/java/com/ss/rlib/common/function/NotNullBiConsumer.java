package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface NotNullBiConsumer<T, U> extends BiConsumer<T, U> {

    @Override
    void accept(@NotNull T first, @NotNull U second);
}
