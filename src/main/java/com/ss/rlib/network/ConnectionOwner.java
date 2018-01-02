package com.ss.rlib.network;

import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.SendablePacket;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * The interface to implement a connection owner.
 *
 * @author JavaSaBr
 */
public interface ConnectionOwner {

    /**
     * Destroy this owner.
     */
    void destroy();

    /**
     * Get the network crypt of this owner.
     *
     * @return the network crypt.
     */
    @NotNull NetworkCrypt getCrypt();

    /**
     * Get the connection.
     *
     * @return the connection.
     */
    @NotNull AsyncConnection getConnection();

    /**
     * Check if the owner is ready to work.
     *
     * @return true if this owner is ready to work.
     */
    boolean isReady();

    /**
     * Read and handle the packet.
     *
     * @param packet the packet.
     * @param buffer the data.
     */
    void readPacket(@NotNull ReadablePacket packet, @NotNull ByteBuffer buffer);

    /**
     * Send the packet to the owner.
     *
     * @param packet the packet.
     */
    void sendPacket(@NotNull SendablePacket packet);
}
