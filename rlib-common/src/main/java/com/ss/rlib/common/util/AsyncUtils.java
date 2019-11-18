package com.ss.rlib.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletionException;

/**
 * @author JavaSaBr
 */
public class AsyncUtils {

    public static <T> @Nullable T skip(@NotNull Throwable throwable) {
        return null;
    }

    public static <T> @Nullable T continueCompletableStage(@Nullable T result, @Nullable Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else if (throwable != null) {
            throw new CompletionException(throwable);
        } else {
            return result;
        }
    }
}
