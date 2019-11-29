package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeSupplier<T> {

    @Nullable T get() throws Exception;
}
