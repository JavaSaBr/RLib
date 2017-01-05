package rlib.network.client.server;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * The interface to implement a server.
 *
 * @author JavaSaBr
 */
@SuppressWarnings("rawtypes")
public interface Server<C extends ServerConnection, RP extends ReadablePacket, SP extends SendablePacket> {

    /**
     * Close.
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
    C getConnection();

    /**
     * @return true of this server connected.
     */
    boolean isConnected();

    /**
     * Read and handle a packet.
     *
     * @param packet the packet.
     * @param buffer the data.
     */
    void readPacket(@NotNull RP packet, @NotNull ByteBuffer buffer);

    /**
     * Send a packet to server.
     *
     * @param packet the packet.
     */
    void sendPacket(@NotNull SP packet);
}
