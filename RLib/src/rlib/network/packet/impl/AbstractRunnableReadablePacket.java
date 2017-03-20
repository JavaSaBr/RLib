package rlib.network.packet.impl;

import java.util.concurrent.ExecutorService;

/**
 * The base implementation of the {@link AbstractReadablePacket} with implementing {@link Runnable}
 * for executing in the {@link ExecutorService}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractRunnableReadablePacket extends AbstractReadablePacket implements Runnable {

    @Override
    public void run() {
        notifyStartedReading();
        try {
            runImpl();
        } catch (final Exception e) {
            LOGGER.warning(this, e);
        } finally {
            notifyFinishedReading();
        }
    }

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
