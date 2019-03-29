package com.ss.rlib.common.function;

/**
 * @param <T> the type of getting value.
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeSupplier<T> {

    /**
     * Gets a result.
     *
     * @return a result.
     */
    T get() throws Exception;
}
