package rlib.network.server.client;

import java.nio.channels.AsynchronousSocketChannel;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.network.AbstractAsynConnection;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;
import rlib.network.server.ServerNetwork;

/**
 * Базовая модель асинхронного конекта.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractClientConnection<T extends Client, R extends ReadeablePacket<T>, S extends SendablePacket<T>> extends AbstractAsynConnection<ServerNetwork, R, S> implements
		ClientConnection<T, R, S> {

	public AbstractClientConnection(ServerNetwork network, AsynchronousSocketChannel channel, Class<S> sendableType) {
		super(network, channel, sendableType);
	}

	protected static final Logger LOGGER = Loggers.getLogger(ClientConnection.class);

	/** клиент пользователя */
	protected T client;

	@Override
	public final T getClient() {
		return client;
	}

	@Override
	public final void setClient(T client) {
		this.client = client;
	}

	@Override
	protected void finish() {
		client.close();
	}

	@Override
	protected void onWrited(S packet) {
	}
}
