package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface NotNullBiPredicate<T, U> extends BiPredicate<T, U> {

    @Override
    boolean test(@NotNull T first, @NotNull U second);
}
