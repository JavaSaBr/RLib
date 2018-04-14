package com.ss.rlib.common.concurrent.task;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a simple task.
 *
 * @param <L> the type parameter
 * @author JavaSaBr
 */
public interface SimpleTask<L> extends CallableTask<Void, L> {

    @Override
    default Void call(@NotNull final L local, final long currentTime) {
        execute(local, currentTime);
        return null;
    }

    /**
     * Execute this task.
     *
     * @param local       the thread local container.
     * @param currentTime the current time.
     */
    void execute(@NotNull L local, long currentTime);
}
