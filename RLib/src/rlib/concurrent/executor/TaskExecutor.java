package rlib.concurrent.executor;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;

import rlib.concurrent.task.CallableTask;
import rlib.concurrent.task.SimpleTask;

/**
 * The interface to implement a task executor.
 *
 * @author JavaSaBr
 */
public interface TaskExecutor<L> {

    /**
     * Execute a simple task.
     *
     * @param task the simple task.
     */
    void execute(@NotNull SimpleTask<L> task);

    /**
     * Submit a callable task.
     *
     * @param task the callable task.
     * @return the reference to the task.
     */
    @NotNull
    <R> Future<R> submit(@NotNull CallableTask<R, L> task);
}
