package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullTripleConsumer<F, S, T> extends TripleConsumer<F, S, T> {

    @Override
    void accept(@NotNull F first, @NotNull S second, @NotNull T third);
}
