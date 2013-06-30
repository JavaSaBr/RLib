package rlib.network.server.client;

import rlib.network.AsynConnection;
import rlib.network.packets.ReadeablePacket;
import rlib.network.packets.SendablePacket;

/**
 * Интерфейс для реализации асинхронного коннекта к игровому клиенту.
 * 
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public interface ClientConnection<T extends Client, R extends ReadeablePacket<T>, S extends SendablePacket<T>> extends AsynConnection<R, S>
{
	/**
	 * @return клиент.
	 */
	public T getClient();
	
	/**
	 * Установка клиента.
	 * 
	 * @param client клиент.
	 */
	public void setClient(T client);
}
