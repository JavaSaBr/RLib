package rlib.network.client;

import rlib.network.AsynchronousNetwork;

import java.net.InetSocketAddress;

/**
 * Интерфейс для реализации модели клиентской асинхронной сети.
 *
 * @author Ronn
 */
public interface ClientNetwork extends AsynchronousNetwork {

    /**
     * Подключение клиента к серверу.
     *
     * @param serverAddress адресс сервера.
     */
    public void connect(InetSocketAddress serverAddress);
}
