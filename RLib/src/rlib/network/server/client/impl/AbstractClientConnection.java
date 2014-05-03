package rlib.network.server.client.impl;

import java.nio.channels.AsynchronousSocketChannel;

import rlib.network.impl.AbstractAsynConnection;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;
import rlib.network.server.ServerNetwork;
import rlib.network.server.client.Client;
import rlib.network.server.client.ClientConnection;

/**
 * Базовая модель асинхронного конекта.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractClientConnection<T extends Client, R extends ReadeablePacket<T>, S extends SendablePacket<T>> extends AbstractAsynConnection<ServerNetwork, R, S> implements
		ClientConnection<T, R, S> {

	/** клиент пользователя */
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
	protected void onWrited(final S packet) {
	}

	@Override
	public final void setClient(final T client) {
		this.client = client;
	}
}
