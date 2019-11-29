package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeFactory<R> {

    @Nullable R get() throws Exception;
}
