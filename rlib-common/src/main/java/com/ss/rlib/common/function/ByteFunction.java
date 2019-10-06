package com.ss.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ByteFunction<R> {

    R apply(byte value);
}
