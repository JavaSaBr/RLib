package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The consumer with 5 arguments.
 *
 * @param <F>  the type parameter
 * @param <S>  the type parameter
 * @param <T>  the type parameter
 * @param <FO> the type parameter
 * @param <FI> the type parameter
 * @param <SX> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SixObjectConsumer<F, S, T, FO, FI, SX> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     * @param third  the third
     * @param fourth the fourth
     * @param five   the five
     * @param six    the six
     */
    void accept(@Nullable F first, @Nullable S second, @Nullable T third, @Nullable FO fourth,
                @Nullable FI five, @Nullable SX six);
}
