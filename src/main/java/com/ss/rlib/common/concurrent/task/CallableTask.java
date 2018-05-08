package com.ss.rlib.common.concurrent.task;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement a callable tasks.
 *
 * @param <R> the type parameter
 * @param <L> the type parameter
 * @author JavaSaBr
 */
@FunctionalInterface
public interface CallableTask<R, L> {

    /**
     * Execute this task.
     *
     * @param local       the thread local container.
     * @param currentTime the current time.
     * @return the result.
     */
    @Nullable
    R call(@NotNull L local, long currentTime);
}
