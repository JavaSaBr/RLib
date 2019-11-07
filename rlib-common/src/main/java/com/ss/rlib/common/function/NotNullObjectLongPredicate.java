package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NotNullObjectLongPredicate<T> extends ObjectLongPredicate<T> {

    @Override
    boolean test(@NotNull T first, long second);
}
