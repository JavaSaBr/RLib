package com.ss.rlib.common.function;

import org.jetbrains.annotations.NotNull;

/**
 * @author JavaSaBr
 */
@FunctionalInterface
public interface NotNullTriplePredicate<F, S, T> extends TriplePredicate<F, S, T> {

    boolean test(@NotNull F first, @NotNull S second, @NotNull T third);
}
