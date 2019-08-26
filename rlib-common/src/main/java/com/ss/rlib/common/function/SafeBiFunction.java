package com.ss.rlib.common.function;

@FunctionalInterface
public interface SafeBiFunction<F, S, R> {

    R apply(F first, S second) throws Exception;
}
