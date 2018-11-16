package com.ss.rlib.common.function;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface SafeRunnable {

    /**
     * Run.
     *
     * @throws Exception the exception
     */
    void run() throws Exception;
}
