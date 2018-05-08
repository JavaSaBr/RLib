package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <F> the type parameter
 * @param <S> the type parameter
 * @param <R> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeBiFunction<F, S, R> {

    /**
     * Apply r.
     *
     * @param first  the first
     * @param second the second
     * @return the r
     * @throws Exception the exception
     */
    @Nullable
    R apply(@Nullable F first, @Nullable S second) throws Exception;
}
