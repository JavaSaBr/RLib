package com.ss.rlib.network;

import com.ss.rlib.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement an async connection.
 *
 * @author JavaSaBr
 */
public interface AsyncConnection {

    /**
     * Get a remote address or 'unkonwn'.
     *
     * @return the remote address or 'unkonwn'.
     */
    @NotNull String getRemoteAddress();

    /**
     * Get a connection's owner.
     *
     * @return the connection's owner,
     */
    @Nullable ConnectionOwner getOwner();

    /**
     * Get a network of this connection.
     *
     * @return the network of this connection.
     */
    @NotNull AsyncNetwork getNetwork();

    /**
     * Get a timestamp of last write/read activity.
     *
     * @return the timestamp of last write/read activity.
     */
    long getLastActivity();

    /**
     * Close this connection if this connection is still opened.
     */
    void close();

    /**
     * Check a closed state of this connection.
     *
     * @return true if this connection is already closed.
     */
    boolean isClosed();

    /**
     * Send a packet to connection's owner.
     *
     * @param packet the writable packet.
     */
    void sendPacket(@NotNull WritablePacket packet);

    /**
     * Activate a process of receiving packets.
     */
    void startRead();

    /**
     * Get a used implementation of a data crypt.
     *
     * @return the used implementation of a data crypt.
     */
    @NotNull NetworkCrypt getCrypt();

    /**
     * Get the byte count of packet size bytes.
     *
     * @return the byte count of packet size bytes.
     */
    int getPacketSizeByteCount();
}
