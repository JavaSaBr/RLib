package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The consumer with 4 arguments.
 *
 * @param <F>  the type parameter
 * @param <S>  the type parameter
 * @param <T>  the type parameter
 * @param <FO> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface FourObjectConsumer<F, S, T, FO> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     * @param third  the third
     * @param fourth the fourth
     */
    void accept(@Nullable F first, @Nullable S second, @Nullable T third, @Nullable FO fourth);
}
