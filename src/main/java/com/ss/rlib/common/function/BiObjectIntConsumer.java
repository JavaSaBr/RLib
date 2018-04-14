package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <F> the type parameter
 * @param <S> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface BiObjectIntConsumer<F, S> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     * @param third  the third
     */
    void accept(@Nullable F first, @Nullable S second, int third);
}
