package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function to consume float and object.
 *
 * @param <T> the consumed object's type.
 * @author JavaSaBr
 */
@FunctionalInterface
public interface FloatObjectConsumer<T> {

    /**
     * Accept the two parameters.
     *
     * @param first  the first.
     * @param second the second.
     */
    void accept(float first, @Nullable T second);
}
