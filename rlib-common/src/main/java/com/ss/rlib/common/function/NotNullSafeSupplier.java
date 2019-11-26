package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullSafeSupplier<T> extends SafeSupplier<T> {

    @Override
    @NotNull T get() throws Exception;
}
