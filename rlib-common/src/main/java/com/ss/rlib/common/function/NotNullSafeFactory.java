package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullSafeFactory<R> extends SafeFactory<R> {

    @Override
    @NotNull R get() throws Exception;
}
