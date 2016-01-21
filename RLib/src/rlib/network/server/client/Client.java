package rlib.network.server.client;

import rlib.network.AsyncConnection;
import rlib.network.packet.ReadeablePacket;
import rlib.network.packet.SendablePacket;

import java.nio.ByteBuffer;

/**
 * Интерфейс для реализации игрового клиента в ММО играх.
 *
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public interface Client<A, P, C extends AsyncConnection, RP extends ReadeablePacket, SP extends SendablePacket> {

    /**
     * Закрыть клиент.
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
     * @return аккаунт этого клиента.
     */
    public A getAccount();

    /**
     * @param account аккаунт клиента.
     */
    public void setAccount(A account);

    /**
     * @return коннект клиента.
     */
    public C getConnection();

    /**
     * @return адресс клиента.
     */
    public String getHostAddress();

    /**
     * @return владелец этого клиента.
     */
    public P getOwner();

    /**
     * @param owner владелец этого клиента.
     */
    public void setOwner(P owner);

    /**
     * @return на связи ли еще клиент.
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
     * Отправка пакета клиенту.
     *
     * @param packet отпраляемый пакет.
     */
    public void sendPacket(SP packet);

    /**
     * Обработка успешного подключения.
     */
    public void successfulConnection();
}
