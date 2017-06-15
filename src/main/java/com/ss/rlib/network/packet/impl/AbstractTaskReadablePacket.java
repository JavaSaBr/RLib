package com.ss.rlib.network.packet.impl;

import org.jetbrains.annotations.NotNull;
import com.ss.rlib.concurrent.executor.TaskExecutor;
import com.ss.rlib.concurrent.task.SimpleTask;

/**
 * The base implementation of the {@link AbstractReadablePacket} with implementing {@link SimpleTask}  for executing in
 * the {@link TaskExecutor}.
 *
 * @param <L> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractTaskReadablePacket<L> extends AbstractReadablePacket implements SimpleTask<L> {

    @Override
    public void execute(@NotNull final L local, final long currentTime) {
        notifyStartedReading();
        try {
            executeImpl(local, currentTime);
        } catch (final Exception e) {
            LOGGER.warning(this, e);
        } finally {
            notifyFinishedReading();
        }
    }

    /**
     * The method for implementing of executing of this packet.
     *
     * @param local       the local
     * @param currentTime the current time
     */
    protected abstract void executeImpl(@NotNull L local, long currentTime);

    /**
     * Is synchronized boolean.
     *
     * @return true if this packet hast to execute in the same thread.
     */
    public boolean isSynchronized() {
        return false;
    }
}
