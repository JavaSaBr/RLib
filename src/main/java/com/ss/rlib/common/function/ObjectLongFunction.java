package com.ss.rlib.common.function;

/**
 * The function.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectLongFunction<T, R> {

    /**
     * Apply r.
     *
     * @param first  the first
     * @param second the second
     * @return the r
     */
    R apply(T first, long second);
}
