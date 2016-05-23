package rlib.network.server.client;

import rlib.network.AsyncConnection;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * Интерфейс для реализации асинхронного коннекта к игровому клиенту.
 *
 * @author Ronn
 */
public interface ClientConnection<T extends Client, R extends ReadablePacket<T>, S extends SendablePacket<T>> extends AsyncConnection<R, S> {

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
