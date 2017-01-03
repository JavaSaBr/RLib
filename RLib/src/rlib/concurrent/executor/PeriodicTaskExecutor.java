package rlib.concurrent.executor;

import org.jetbrains.annotations.NotNull;

import rlib.concurrent.task.PeriodicTask;

/**
 * The interface to implement a periodic task executor.
 *
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
