package rlib.network.server.client.impl;

import java.nio.channels.AsynchronousSocketChannel;

import rlib.network.impl.AbstractAsyncConnection;
import rlib.network.packet.ReadeablePacket;
import rlib.network.packet.SendablePacket;
import rlib.network.server.ServerNetwork;
import rlib.network.server.client.Client;
import rlib.network.server.client.ClientConnection;

/**
 * Базовая реализация асинхронного клиентского подключения.
 *
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractClientConnection<T extends Client, R extends ReadeablePacket<T>, S extends SendablePacket<T>> extends AbstractAsyncConnection<ServerNetwork, R, S> implements ClientConnection<T, R, S> {

    /**
     * Подключенный клиент.
     */
    protected T client;

    public AbstractClientConnection(final ServerNetwork network, final AsynchronousSocketChannel channel, final Class<S> sendableType) {
        super(network, channel, sendableType);
    }

    @Override
    protected void finish() {
        client.close();
    }

    @Override
    public final T getClient() {
        return client;
    }

    @Override
    public final void setClient(final T client) {
        this.client = client;
    }

    @Override
    protected void onWrote(final S packet) {
    }
}
