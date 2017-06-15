package com.ss.rlib.function;

import org.jetbrains.annotations.Nullable;

/**
 * The function.
 *
 * @param <T> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeConsumer<T> {

    /**
     * Accept.
     *
     * @param argument the argument
     * @throws Exception the exception
     */
    void accept(@Nullable T argument) throws Exception;
}
