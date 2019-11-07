package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullLongObjectPredicate<T> extends LongObjectPredicate<T> {

    boolean test(long first, @NotNull T second);
}
