package com.ss.rlib.common.function;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface ObjectIntPredicate<T> {

    boolean test(T first, int second);
}
