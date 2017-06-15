package com.ss.rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <F> the type parameter
 * @param <S> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeBiConsumer<F, S> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     * @throws Exception the exception
     */
    void accept(@Nullable F first, @Nullable S second) throws Exception;
}
