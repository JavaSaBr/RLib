package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullIntObjectPredicate<T> extends IntObjectPredicate<T> {

    boolean test(int first, @NotNull T second);
}
