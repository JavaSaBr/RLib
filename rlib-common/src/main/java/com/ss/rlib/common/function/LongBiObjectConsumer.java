package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface LongBiObjectConsumer<S, T> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     * @param third  the third
     */
    void accept(long first, @Nullable S second, @Nullable T third);
}
