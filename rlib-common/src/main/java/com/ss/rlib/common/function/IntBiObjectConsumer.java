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
public interface IntBiObjectConsumer<S, T> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     * @param third  the third
     */
    void accept(int first, @Nullable S second, @Nullable T third);
}
