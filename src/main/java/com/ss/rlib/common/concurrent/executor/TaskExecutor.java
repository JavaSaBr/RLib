package com.ss.rlib.common.concurrent.executor;

import com.ss.rlib.common.concurrent.task.CallableTask;
import com.ss.rlib.common.concurrent.task.SimpleTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;

/**
 * The interface to implement a task executor.
 *
 * @param <L> the type parameter
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
     * @param <R>  the type parameter
     * @param task the callable task.
     * @return the reference to the task.
     */
    @NotNull
    <R> Future<R> submit(@NotNull CallableTask<R, L> task);
}
