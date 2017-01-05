package rlib.network.server.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

import rlib.network.AsyncConnection;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * The interface to implement a client.
 *
 * @author JavaSaBr
 */
@SuppressWarnings("rawtypes")
public interface Client<A, P, C extends AsyncConnection, RP extends ReadablePacket, SP extends SendablePacket> {

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
     * @return the client account.
     */
    @Nullable
    A getAccount();

    /**
     * @param account the client account.
     */
    void setAccount(@Nullable A account);

    /**
     * @return the client connection.
     */
    @NotNull
    C getConnection();

    /**
     * @return the host address.
     */
    @NotNull
    String getHostAddress();

    /**
     * @return the owner.
     */
    @Nullable
    P getOwner();

    /**
     * @param owner the owner.
     */
    void setOwner(@Nullable P owner);

    /**
     * @return true if this connection is connected.
     */
    boolean isConnected();

    /**
     * Read and handle a packet.
     *
     * @param packet the packet.
     * @param buffer the read buffer.
     */
    void readPacket(@NotNull RP packet, @NotNull ByteBuffer buffer);

    /**
     * Send a packet to a client.
     *
     * @param packet the packet.
     */
    void sendPacket(@NotNull SP packet);

    /**
     * Handle successful connect.
     */
    void successfulConnection();
}
