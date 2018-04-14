package com.ss.rlib.common.concurrent.executor;

import com.ss.rlib.common.concurrent.task.PeriodicTask;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a periodic task executor.
 *
 * @param <T> the type parameter
 * @param <L> the type parameter
 * @author JavaSaBr
 */
public interface PeriodicTaskExecutor<T extends PeriodicTask<L>, L> {

    /**
     * Add a periodic task to execute.
     *
     * @param task the periodic task.
     */
    void addTask(@NotNull T task);

    /**
     * Remove a periodic task from executing.
     *
     * @param task the periodic task.
     */
    void removeTask(@NotNull T task);
}
