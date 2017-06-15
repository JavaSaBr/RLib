package com.ss.rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <F> the type parameter
 * @param <R> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeFunction<F, R> {

    /**
     * Apply r.
     *
     * @param first the first
     * @return the r
     * @throws Exception the exception
     */
    @Nullable
    R apply(@Nullable F first) throws Exception;
}
