package com.ss.rlib.common.function;

@FunctionalInterface
public interface SafeFunction<F, R> {

    R apply(F first) throws Exception;
}
