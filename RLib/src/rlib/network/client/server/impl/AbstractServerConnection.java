package rlib.network.client.server.impl;

import java.nio.channels.AsynchronousSocketChannel;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.client.ClientNetwork;
import rlib.network.client.server.Server;
import rlib.network.client.server.ServerConnection;
import rlib.network.impl.AbstractAsyncConnection;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * Базовая модель асинхронного конекта к серверу.
 *
 * @author JavaSaBr
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServerConnection<T extends Server, R extends ReadablePacket<T>, S extends SendablePacket<T>> extends AbstractAsyncConnection<ClientNetwork, R, S> implements ServerConnection<T, R, S> {

    protected static final Logger LOGGER = LoggerManager.getLogger(ServerConnection.class);

    /**
     * Сервер.
     */
    protected T server;

    public AbstractServerConnection(final ClientNetwork network, final AsynchronousSocketChannel channel, final Class<S> sendableType) {
        super(network, channel, sendableType);
    }

    @Override
    protected void finish() {

        final T server = getServer();

        if (server != null) {
            server.close();
        } else if (!isClosed()) {
            close();
        }
    }

    @Override
    public T getServer() {
        return server;
    }

    @Override
    public void setServer(final T server) {
        this.server = server;
    }

    @Override
    protected void completed(final S packet) {
    }

    @Override
    public String toString() {
        return "AbstractServerConnection{" +
                "server=" + server +
                "} " + super.toString();
    }
}
