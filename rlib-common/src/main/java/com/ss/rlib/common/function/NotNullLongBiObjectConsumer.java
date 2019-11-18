package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullLongBiObjectConsumer<S, T> extends LongBiObjectConsumer<S, T> {

    @Override
    void accept(long first, @NotNull S second, @NotNull T third);
}
