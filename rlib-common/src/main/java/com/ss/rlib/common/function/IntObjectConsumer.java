package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface IntObjectConsumer<T> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     */
    void accept(int first, @Nullable T second);
}
