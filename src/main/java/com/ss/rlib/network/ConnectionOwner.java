package com.ss.rlib.network;

import org.jetbrains.annotations.NotNull;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.SendablePacket;

import java.nio.ByteBuffer;

/**
 * The interface to implement a connection owner.
 *
 * @author JavaSaBr
 */
public interface ConnectionOwner {

    /**
     * Close this server.
     */
    void close();

    /**
     * Decrypt data.
     *
     * @param data   the data.
     * @param offset the offset.
     * @param length the length.
     */
    void decrypt(@NotNull ByteBuffer data, int offset, int length);

    /**
     * Encrypt data.
     *
     * @param data   the data.
     * @param offset the offset.
     * @param length the length.
     */
    void encrypt(@NotNull ByteBuffer data, int offset, int length);

    /**
     * Get a connection to server.
     *
     * @return the connection.
     */
    @NotNull
    AsyncConnection getConnection();

    /**
     * Is connected boolean.
     *
     * @return true of this server connected.
     */
    boolean isConnected();

    /**
     * Read and handle a packet.
     *
     * @param packet the packet.
     * @param buffer the data.
     */
    void readPacket(@NotNull ReadablePacket packet, @NotNull ByteBuffer buffer);

    /**
     * Send a packet to server.
     *
     * @param packet the packet.
     */
    void sendPacket(@NotNull SendablePacket packet);
}
