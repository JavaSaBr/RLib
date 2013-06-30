package rlib.network.client.server;

import java.nio.channels.AsynchronousSocketChannel;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.network.AbstractAsynConnection;
import rlib.network.client.ClientNetwork;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;


/**
 * Базовая модель асинхронного конекта к серверу.
 *
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServerConnection<T extends Server, R extends ReadeablePacket<T>, S extends SendablePacket<T>> extends AbstractAsynConnection<ClientNetwork, R, S> implements ServerConnection<T, R, S>
{
	protected static final Logger log = Loggers.getLogger(ServerConnection.class);

	/** сервер */
	protected T server;

	public AbstractServerConnection(ClientNetwork network, AsynchronousSocketChannel channel, Class<S> sendableType)
	{
		super(network, channel, sendableType);
	}

	@Override
	protected void finish()
	{
		T server = getServer();
		
		if(server != null)
			server.close();
		else if(!isClosed())
			close();
	}
	
	@Override
	public T getServer()
	{
		return server;
	}
	
	@Override
	protected void onWrited(S packet)
	{
		//TODO
	}
	
	@Override
	public void setServer(T server)
	{
		this.server = server;
	}
}
