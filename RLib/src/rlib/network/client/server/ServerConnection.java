package rlib.network.client.server;

import org.jetbrains.annotations.Nullable;

import rlib.network.AsyncConnection;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * The interface to implement a server connection.
 *
 * @author JavaSaBr
 */
public interface ServerConnection<T extends Server, R extends ReadablePacket, S extends SendablePacket>
        extends AsyncConnection<R, S> {

    /**
     * @return the server.
     */
    @Nullable
    T getServer();

    /**
     * @param server the server.
     */
    void setServer(@Nullable T server);
}
