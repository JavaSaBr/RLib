package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullIntBiObjectConsumer<S, T> extends IntBiObjectConsumer<S, T> {

    @Override
    void accept(int first, @NotNull S second, @NotNull T third);
}
