package rlib.network.server.client;

import org.jetbrains.annotations.Nullable;

import rlib.network.AsyncConnection;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * The interface to implement a client connection.
 *
 * @author JavaSaBr
 */
public interface ClientConnection<T extends Client, R extends ReadablePacket, S extends SendablePacket> extends AsyncConnection<R, S> {

    /**
     * @return the client.
     */
    @Nullable
    T getClient();

    /**
     * The client.
     *
     * @param client the client.
     */
    void setClient(@Nullable T client);
}
