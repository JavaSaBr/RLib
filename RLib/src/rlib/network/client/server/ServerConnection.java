package rlib.network.client.server;

import rlib.network.AsyncConnection;
import rlib.network.packet.ReadeablePacket;
import rlib.network.packet.SendablePacket;

/**
 * Интерфейс для реализации асинхронного подключения к серверу.
 *
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public interface ServerConnection<T extends Server, R extends ReadeablePacket<T>, S extends SendablePacket<T>> extends AsyncConnection<R, S> {

    /**
     * @return сервер.
     */
    public T getServer();

    /**
     * @param server сервер.
     */
    public void setServer(T server);
}
