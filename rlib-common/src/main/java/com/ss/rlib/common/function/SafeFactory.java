package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <R> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeFactory<R> {

    /**
     * Get r.
     *
     * @return the r
     * @throws Exception the exception
     */
    @Nullable
    R get() throws Exception;
}
