package com.ss.rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectIntConsumer<T> {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     */
    void accept(@Nullable T first, int second);
}
