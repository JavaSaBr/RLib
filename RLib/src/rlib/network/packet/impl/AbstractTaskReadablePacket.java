package rlib.network.packet.impl;

import org.jetbrains.annotations.NotNull;
import rlib.concurrent.executor.TaskExecutor;
import rlib.concurrent.task.SimpleTask;

/**
 * The base implementation of the {@link AbstractReadablePacket} with implementing {@link SimpleTask}  for executing in
 * the {@link TaskExecutor}.
 *
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
     */
    protected abstract void executeImpl(@NotNull L local, long currentTime);

    /**
     * @return true if this packet hast to execute in the same thread.
     */
    public boolean isSynchronized() {
        return false;
    }
}
