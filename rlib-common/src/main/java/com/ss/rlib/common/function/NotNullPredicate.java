package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@FunctionalInterface
public interface NotNullPredicate<T> extends Predicate<T> {

    @Override
    boolean test(@NotNull T object);
}
