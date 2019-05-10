package com.ss.rlib.network;

import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

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
     * Get length of packet's header with packet's data length.
     *
     * @return the length of packet's header with packet's data length.
     */
    int getPacketLengthHeaderSize();

    /**
     * Get length of packet's header with packet's id.
     *
     * @return the length of packet's header with packet's id.
     */
    int getPacketIdHeaderSize();

    /**
     * Add the handler of each read packet from this owner.
     *
     * @param handler the handler.
     * @param <P>     the readable packet's type.
     * @return the added handler.
     */
    <P extends ReadablePacket> @NotNull Consumer<P> addPacketHandler(@NotNull Consumer<P> handler);

    /**
     * Remove the handler.
     *
     * @param handler the handler.
     */
    void removePacketHandler(@NotNull Consumer<? extends ReadablePacket> handler);
}
