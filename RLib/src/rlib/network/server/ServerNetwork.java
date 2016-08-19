package rlib.network.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import rlib.network.AsynchronousNetwork;

/**
 * Интерфейс для реализации модели асинхронной серверной сети.
 *
 * @author JavaSaBr
 */
public interface ServerNetwork extends AsynchronousNetwork {

    /**
     * Отдать хандлер на ожидание конекта клиента.
     *
     * @param attachment дополнительный объект.
     * @param handler    обработчик.
     */
    public <A> void accept(A attachment, CompletionHandler<AsynchronousSocketChannel, ? super A> handler);

    /**
     * Бинд сокета под сеть.
     *
     * @param address адресс сокета.
     */
    public void bind(SocketAddress address) throws IOException;
}
