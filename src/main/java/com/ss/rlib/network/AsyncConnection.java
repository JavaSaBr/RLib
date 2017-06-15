package com.ss.rlib.network;

import com.ss.rlib.concurrent.lock.Lockable;
import com.ss.rlib.network.packet.SendablePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement an async connection.
 *
 * @author JavaSaBr
 */
public interface AsyncConnection extends Lockable {

    /**
     * Get a connection owner.
     *
     * @return the connection owner.
     */
    @Nullable
    ConnectionOwner getOwner();

    /**
     * Set the new connection owner.
     *
     * @param owner the connection owner.
     */
    void setOwner(@Nullable ConnectionOwner owner);

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
     * Is closed boolean.
     *
     * @return true if this connection is closed.
     */
    boolean isClosed();

    /**
     * Add a packet to queue to send.
     *
     * @param packet the sendable packet.
     */
    void sendPacket(@NotNull SendablePacket packet);

    /**
     * Activate a reading packets process.
     */
    void startRead();
}
