package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullFloatBiObjectConsumer<S, T> extends FloatBiObjectConsumer<S, T> {

    @Override
    void accept(float first, @NotNull S second, @NotNull T third);
}
