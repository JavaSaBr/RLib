package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectFloatFunction<T, R> {

    /**
     * Apply r.
     *
     * @param first  the first
     * @param second the second
     * @return the r
     */
    @Nullable
    R apply(@Nullable T first, float second);
}
