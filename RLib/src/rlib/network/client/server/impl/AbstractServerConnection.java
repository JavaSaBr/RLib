package rlib.network.client.server.impl;

import java.nio.channels.AsynchronousSocketChannel;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.client.ClientNetwork;
import rlib.network.client.server.Server;
import rlib.network.client.server.ServerConnection;
import rlib.network.impl.AbstractAsynConnection;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;

/**
 * Базовая модель асинхронного конекта к серверу.
 *
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServerConnection<T extends Server, R extends ReadeablePacket<T>, S extends SendablePacket<T>> extends AbstractAsynConnection<ClientNetwork, R, S> implements
		ServerConnection<T, R, S> {

	protected static final Logger LOGGER = LoggerManager.getLogger(ServerConnection.class);

	/** сервер */
	protected T server;

	public AbstractServerConnection(ClientNetwork network, AsynchronousSocketChannel channel, Class<S> sendableType) {
		super(network, channel, sendableType);
	}

	@Override
	protected void finish() {

		T server = getServer();

		if(server != null) {
			server.close();
		} else if(!isClosed()) {
			close();
		}
	}

	@Override
	public T getServer() {
		return server;
	}

	@Override
	protected void onWrited(S packet) {
	}

	@Override
	public void setServer(T server) {
		this.server = server;
	}
}
