package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <F> the type parameter
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface TriplePredicate<F, S, T> {

    /**
     * Test boolean.
     *
     * @param first  the first
     * @param second the second
     * @param third  the third
     * @return the boolean
     */
    boolean test(@Nullable F first, @Nullable S second, @Nullable T third);
}
