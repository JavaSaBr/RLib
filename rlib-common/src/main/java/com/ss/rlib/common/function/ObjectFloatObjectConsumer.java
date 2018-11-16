package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The consumer with 3 arguments.
 *
 * @param <F> the type parameter
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectFloatObjectConsumer<F, T> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     * @param third  the third
     */
    void accept(@Nullable F first, float second, @Nullable T third);
}
