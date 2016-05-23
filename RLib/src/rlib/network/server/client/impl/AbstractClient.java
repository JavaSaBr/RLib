package rlib.network.server.client.impl;

import java.nio.ByteBuffer;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsyncConnection;
import rlib.network.GameCrypt;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;
import rlib.network.server.client.Client;

/**
 * Базовая реализация клиента для сервера.
 *
 * @author Ronn
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractClient<A, O, C extends AsyncConnection, T extends GameCrypt, RP extends ReadablePacket, SP extends SendablePacket> implements Client<A, O, C, RP, SP> {

    protected static final Logger LOGGER = LoggerManager.getLogger(Client.class);

    /**
     * Подкючение клиента к серверу
     */
    protected volatile C connection;

    /**
     * Владелец подключения.
     */
    protected volatile O owner;

    /**
     * Аккаунт клиента.
     */
    protected volatile A account;

    /**
     * Криптор клиента.
     */
    protected volatile T crypt;

    /**
     * Закрыт ли клиент.
     */
    protected volatile boolean closed;

    public AbstractClient(final C connection, final T crypt) {
        this.connection = connection;
        this.crypt = crypt;
    }

    @Override
    public void close() {

        final C connection = getConnection();

        if (connection != null) {
            connection.close();
        }

        setClosed(true);
    }

    @Override
    public void decrypt(final ByteBuffer data, final int offset, final int length) {
        crypt.decrypt(data.array(), offset, length);
    }

    @Override
    public void encrypt(final ByteBuffer data, final int offset, final int length) {
        crypt.encrypt(data.array(), offset, length);
    }

    /**
     * @param packet выполняемый пакет.
     */
    protected abstract void execute(RP packet);

    @Override
    public final A getAccount() {
        return account;
    }

    @Override
    public final void setAccount(final A account) {
        this.account = account;
    }

    @Override
    public final C getConnection() {
        return connection;
    }

    @Override
    public final O getOwner() {
        return owner;
    }

    @Override
    public final void setOwner(final O owner) {
        this.owner = owner;
    }

    /**
     * @return закрыт ли клиент.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @param closed закрыт ли клиент.
     */
    protected void setClosed(final boolean closed) {
        this.closed = closed;
    }

    @Override
    public final boolean isConnected() {
        return !isClosed() && connection != null && !connection.isClosed();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void readPacket(final RP packet, final ByteBuffer buffer) {

        if (packet == null) {
            return;
        }

        packet.setOwner(this);
        packet.setBuffer(buffer);
        try {
            if (packet.read()) execute(packet);
        } finally {
            packet.setBuffer(null);
        }
    }

    @Override
    public final void sendPacket(final SendablePacket packet) {

        if (isClosed()) {
            return;
        }

        final C connection = getConnection();

        if (connection != null) {
            connection.sendPacket(packet);
        }
    }

    @Override
    public void successfulConnection() {
        LOGGER.info(this, getHostAddress() + " successful connection.");
    }

    /**
     * Смена сетевого подключения для этого клиента.
     *
     * @param connection сетевое подключение.
     */
    protected void switchTo(final C connection) {

        final C current = getConnection();

        if (current == connection) {
            return;
        }

        if (!current.isClosed()) {
            current.close();
        }

        this.connection = connection;
    }

    @Override
    public String toString() {
        return "AbstractClient{" +
                ", owner=" + owner +
                ", account=" + account +
                ", crypt=" + crypt +
                ", closed=" + closed +
                '}';
    }
}
