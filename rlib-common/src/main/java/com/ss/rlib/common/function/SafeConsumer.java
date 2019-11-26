package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeConsumer<T> {

    void accept(@Nullable T argument) throws Exception;
}
