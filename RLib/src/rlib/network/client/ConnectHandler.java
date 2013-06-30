package rlib.network.client;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Интерфейс для реализации обработчика подключения к серверу.
 * 
 * @author Ronn
 */
public abstract class ConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel>
{
	@Override
	public void completed(Void result, AsynchronousSocketChannel attachment)
	{
		onConnect(attachment);
	}
	
	@Override
	public void failed(Throwable exc, AsynchronousSocketChannel attachment)
	{
		onFailed(exc);
	}
	
	/**
	 * Обработка подключения к серверу.
	 * 
	 * @param channel канал с сервером.
	 */
	protected abstract void onConnect(AsynchronousSocketChannel channel);
	
	/**
	 * Обработка ошибки подключения к серверу.
	 * 
	 * @param exc ошибка подключения.
	 */
	protected abstract void onFailed(Throwable exc);
}
