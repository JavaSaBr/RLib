package com.ss.rlib.function;

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
