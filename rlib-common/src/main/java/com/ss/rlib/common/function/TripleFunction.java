package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <F> the type parameter
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @param <R> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface TripleFunction<F, S, T, R> {

    /**
     * Apply r.
     *
     * @param first  the first
     * @param second the second
     * @param third  the third
     * @return the r
     */
    @Nullable
    R apply(@Nullable F first, @Nullable S second, @Nullable T third);
}
