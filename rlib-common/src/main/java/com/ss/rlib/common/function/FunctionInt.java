package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface FunctionInt<T> {

    /**
     * Apply int.
     *
     * @param first the first
     * @return the int
     */
    int apply(@Nullable T first);
}
