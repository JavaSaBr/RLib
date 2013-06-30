package rlib.network.client.server;

import rlib.network.AsynConnection;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;

/**
 * Интерфейс для реализации асинхронного коннекта к серверу.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public interface ServerConnection<T extends Server, R extends ReadeablePacket<T>, S extends SendablePacket<T>> extends AsynConnection<R, S>
{
	/**
	 * @return сервер.
	 */
	public T getServer();
	
	/**
	 * @param server сервер.
	 */
	public void setServer(T server);
}
