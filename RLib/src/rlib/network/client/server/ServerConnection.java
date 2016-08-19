package rlib.network.client.server;

import rlib.network.AsyncConnection;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * Интерфейс для реализации асинхронного подключения к серверу.
 *
 * @author JavaSaBr
 */
@SuppressWarnings("rawtypes")
public interface ServerConnection<T extends Server, R extends ReadablePacket<T>, S extends SendablePacket<T>> extends AsyncConnection<R, S> {

    /**
     * @return сервер.
     */
    public T getServer();

    /**
     * @param server сервер.
     */
    public void setServer(T server);
}
