package com.ss.rlib.common.function;

/**
 * The function.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface BooleanFloatConsumer {

    /**
     * Accept.
     *
     * @param first  the first
     * @param second the second
     */
    void accept(boolean first, float second);
}
