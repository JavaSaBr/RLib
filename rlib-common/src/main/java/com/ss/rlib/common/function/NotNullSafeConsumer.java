package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullSafeConsumer<T> extends SafeConsumer<T> {

    @Override
    void accept(@NotNull T argument) throws Exception;
}
