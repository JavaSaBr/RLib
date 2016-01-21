package rlib.network.client.server.impl;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.client.ClientNetwork;
import rlib.network.client.server.Server;
import rlib.network.client.server.ServerConnection;
import rlib.network.impl.AbstractAsyncConnection;
import rlib.network.packet.ReadeablePacket;
import rlib.network.packet.SendablePacket;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Базовая модель асинхронного конекта к серверу.
 *
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServerConnection<T extends Server, R extends ReadeablePacket<T>, S extends SendablePacket<T>> extends AbstractAsyncConnection<ClientNetwork, R, S> implements ServerConnection<T, R, S> {

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
    protected void onWrote(final S packet) {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [network=" + network + ", channel=" + channel + ", closed=" + closed + ", config=" + config + ", lastActive=" + lastActive + "]";
    }
}
