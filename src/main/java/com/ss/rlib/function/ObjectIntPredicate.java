package com.ss.rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * THe function.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectIntPredicate<T> {

    /**
     * Test boolean.
     *
     * @param fisrt  the fisrt
     * @param second the second
     * @return the boolean
     */
    boolean test(@Nullable T fisrt, int second);
}
