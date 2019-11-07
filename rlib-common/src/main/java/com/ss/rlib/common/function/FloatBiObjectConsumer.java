package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface FloatBiObjectConsumer<S, T> {

    void accept(float first, @Nullable S second, @Nullable T third);
}
