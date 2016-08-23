package rlib.network.packet.impl;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;

import rlib.util.pools.ReusablePool;

/**
 * The base implementation of the {@link AbstractReadablePacket} with implementing {@link Runnable}
 * for executing in the {@link ExecutorService}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractRunnableReadablePacket<C> extends AbstractReadablePacket<C> implements Runnable {

    @Override
    public void run() {
        notifyStartedReading();
        try {
            runImpl();
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
    protected abstract void runImpl();

    /**
     * @return true if this packet hast to execute in the same thread.
     */
    public boolean isSynchronized() {
        return false;
    }
}
