package rlib.network.client.server;

import java.nio.ByteBuffer;

import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * Интерфейс для реализации сервера для игрового клиента.
 *
 * @author JavaSaBr
 */
@SuppressWarnings("rawtypes")
public interface Server<C extends ServerConnection, RP extends ReadablePacket, SP extends SendablePacket> {

    /**
     * Отключиться от сервера.
     */
    public void close();

    /**
     * Декриптовать массив байтов.
     *
     * @param data   массив байтов.
     * @param offset отступ от начала массива.
     * @param length размер массива, который нужно декриптовать.
     */
    public void decrypt(ByteBuffer data, int offset, int length);

    /**
     * Закриптовать массив байтов.
     *
     * @param data   массив байтов.
     * @param offset отступ от начала массива.
     * @param length размер массива, который нужно закриптовать.
     */
    public void encrypt(ByteBuffer data, int offset, int length);

    /**
     * @return коннект к серверу.
     */
    public C getConnection();

    /**
     * @return на связи ли еще сервер.
     */
    public boolean isConnected();

    /**
     * Читаем пакет и добавляем в очередь на обработку.
     *
     * @param packet читаемый пакет.
     * @param buffer читаемый буффер данных.
     */
    public void readPacket(RP packet, ByteBuffer buffer);

    /**
     * Отправка пакета серверу.
     *
     * @param packet отпраляемый пакет.
     */
    public void sendPacket(SP packet);
}
