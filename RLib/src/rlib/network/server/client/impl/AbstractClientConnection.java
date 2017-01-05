package rlib.network.server.client.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.AsynchronousSocketChannel;

import rlib.network.impl.AbstractAsyncConnection;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;
import rlib.network.server.ServerNetwork;
import rlib.network.server.client.Client;
import rlib.network.server.client.ClientConnection;

/**
 * The base implementation of a client connection.
 *
 * @author JavaSaBr
 */
public abstract class AbstractClientConnection<T extends Client, R extends ReadablePacket, S extends SendablePacket>
        extends AbstractAsyncConnection<ServerNetwork, R, S> implements ClientConnection<T, R, S> {

    /**
     * The client.
     */
    @Nullable
    protected T client;

    public AbstractClientConnection(@NotNull final ServerNetwork network, @NotNull final AsynchronousSocketChannel channel,
                                    @NotNull final Class<S> sendableType) {
        super(network, channel, sendableType);
    }

    @Override
    protected void finish() {
        final T client = getClient();
        if (client != null) client.close();
    }

    @Nullable
    @Override
    public final T getClient() {
        return client;
    }

    @Override
    public final void setClient(@Nullable final T client) {
        this.client = client;
    }

    @Override
    protected void completed(@NotNull final S packet) {
    }

    @Override
    public String toString() {
        return "AbstractClientConnection{" +
                "client=" + client +
                "} " + super.toString();
    }
}
