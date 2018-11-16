package com.ss.rlib.common.function;

/**
 * The function to consume float values.
 *
 * @author JavaSaBr
 */
@FunctionalInterface
public interface FloatConsumer {

    /**
     * Consume the float value.
     *
     * @param value the value.
     */
    void consume(float value);
}
