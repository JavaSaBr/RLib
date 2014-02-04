package rlib.network.server;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import rlib.logging.Logger;
import rlib.logging.Loggers;

/**
 * Базовая модель обработчика принятия конектов.
 * 
 * @author Ronn
 */
public abstract class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

	protected static final Logger LOGGER = Loggers.getLogger(AcceptHandler.class);

	@Override
	public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel serverChannel) {
		serverChannel.accept(serverChannel, this);
		onAccept(result);
	}

	@Override
	public void failed(Throwable exc, AsynchronousServerSocketChannel serverChannel) {
		serverChannel.accept(serverChannel, this);
		onFailed(exc);
	}

	/**
	 * Обработка подключения клиента.
	 * 
	 * @param channel канал к клиенту.
	 */
	protected abstract void onAccept(AsynchronousSocketChannel channel);

	/**
	 * Обработка ошибки подключения клиента.
	 * 
	 * @param exc ошибка подключения.
	 */
	protected abstract void onFailed(Throwable exc);
}
