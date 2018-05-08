package com.ss.rlib.common.function;

import org.jetbrains.annotations.Nullable;

/**
 * ФThe function.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface LongObjectPredicate<T> {

    /**
     * Test boolean.
     *
     * @param first  the first
     * @param second the second
     * @return the boolean
     */
    boolean test(long first, @Nullable T second);
}
