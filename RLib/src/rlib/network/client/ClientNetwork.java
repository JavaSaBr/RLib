package rlib.network.client;

import java.net.InetSocketAddress;

import rlib.network.AsynchronousNetwork;


/**
 * Интерфейс для реализации модели клиентской асинхронной сети.
 * 
 * @author Ronn
 */
public interface ClientNetwork extends AsynchronousNetwork
{
	/**
	 * Подключение клиента к серверу.
	 * 
	 * @param serverAddress адресс сервера.
	 */
	public void connect(InetSocketAddress serverAddress);
}
