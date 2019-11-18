package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@FunctionalInterface
public interface NotNullConsumer<T> extends Consumer<T> {

    @Override
    void accept(@NotNull T object);
}
