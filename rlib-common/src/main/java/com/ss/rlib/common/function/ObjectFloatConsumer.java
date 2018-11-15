package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectFloatConsumer<T> {

    /**
     * Apply.
     *
     * @param first  the first
     * @param second the second
     */
    void apply(@Nullable T first, float second);
}
