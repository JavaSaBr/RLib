package com.ss.rlib.common.network;

import com.ss.rlib.common.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement an async connection.
 *
 * @author JavaSaBr
 */
public interface AsyncConnection {

    /**
     * Get a connection owner.
     *
     * @return the connection owner.
     */
    @Nullable ConnectionOwner getOwner();

    /**
     * Get the connection network.
     *
     * @return the connection network.
     */
    @NotNull AsyncNetwork getNetwork();

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
     * Check if the connection is closed.
     *
     * @return true if this connection is closed.
     */
    boolean isClosed();

    /**
     * Send the packet to connection owner.
     *
     * @param packet the sendable packet.
     */
    void sendPacket(@NotNull WritablePacket packet);

    /**
     * Activate the process of receiving packets.
     */
    void startRead();
}
