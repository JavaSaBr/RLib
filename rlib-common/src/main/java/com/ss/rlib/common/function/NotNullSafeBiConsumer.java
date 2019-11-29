package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullSafeBiConsumer<F, S> extends SafeBiConsumer<F, S> {

    @Override
    void accept(@NotNull F first, @NotNull S second) throws Exception;
}
