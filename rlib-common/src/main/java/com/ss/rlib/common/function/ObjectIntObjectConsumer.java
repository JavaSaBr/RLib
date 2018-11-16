package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <F> the type parameter
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectIntObjectConsumer<F, T> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     * @param third  the third
     */
    void accept(@Nullable F first, int second, @Nullable T third);
}
