package rlib.network.packet.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rlib.concurrent.executor.TaskExecutor;
import rlib.concurrent.task.SimpleTask;
import rlib.util.pools.ReusablePool;

/**
 * The base implementation of the {@link AbstractReadablePacket} with implementing {@link SimpleTask}  for executing in
 * the {@link TaskExecutor}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractTaskReadablePacket<C, L> extends AbstractReadablePacket<C> implements SimpleTask<L> {

    @Override
    public void execute(@NotNull final L local, final long currentTime) {
        notifyStartedReading();
        try {
            executeImpl(local, currentTime);
        } catch (final Exception e) {
            LOGGER.warning(this, e);
        } finally {
            notifyFinishedReading();
            final ReusablePool pool = getPool();
            if (pool != null) pool.put(this);
        }
    }

    /**
     * Gets the pool for storing executed packets.
     *
     * @return the pool for storing executed packets or null.
     */
    @Nullable
    protected abstract ReusablePool getPool();

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
