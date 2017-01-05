package rlib.network;

import org.jetbrains.annotations.NotNull;

import rlib.concurrent.lock.Lockable;

/**
 * The interface to implement an async connection.
 *
 * @author JavaSaBr
 */
public interface AsyncConnection<R, S> extends Lockable {

    /**
     * Close this connection.
     */
    void close();

    /**
     * Get time of last activity.
     *
     * @return the time of last activity.
     */
    long getLastActivity();

    /**
     * Set time of last activity.
     *
     * @param lastActivity the time of last activity.
     */
    void setLastActivity(long lastActivity);

    /**
     * @return true if this connection is closed.
     */
    boolean isClosed();

    /**
     * Add a packet to queue to send.
     *
     * @param packet the send packet.
     */
    void sendPacket(@NotNull S packet);

    /**
     * Activate a read process.
     */
    void startRead();
}
