package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullObjectIntPredicate<T> extends ObjectIntPredicate<T> {

    @Override
    boolean test(@NotNull T first, int second);
}
