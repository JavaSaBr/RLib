package rlib.network.client;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Интерфейс для реализации обработчика подключения к серверу.
 *
 * @author Ronn
 */
public interface ConnectHandler extends CompletionHandler<Void, AsynchronousSocketChannel> {

    @Override
    public default void completed(final Void result, final AsynchronousSocketChannel attachment) {
        onConnect(attachment);
    }

    @Override
    public default void failed(final Throwable exc, final AsynchronousSocketChannel attachment) {
        onFailed(exc);
    }

    /**
     * Обработка подключения к серверу.
     *
     * @param channel канал с сервером.
     */
    public void onConnect(AsynchronousSocketChannel channel);

    /**
     * Обработка ошибки подключения к серверу.
     *
     * @param exc ошибка подключения.
     */
    public void onFailed(Throwable exc);
}
