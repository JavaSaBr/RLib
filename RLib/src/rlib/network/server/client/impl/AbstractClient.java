package rlib.network.server.client.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.AsyncConnection;
import rlib.network.NetworkCrypt;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;
import rlib.network.server.client.Client;

/**
 * The base implementation of a client.
 *
 * @author JavaSaBr
 */
public abstract class AbstractClient<A, O, C extends AsyncConnection, T extends NetworkCrypt, RP extends ReadablePacket, SP extends SendablePacket> implements Client<A, O, C, RP, SP> {

    protected static final Logger LOGGER = LoggerManager.getLogger(Client.class);

    /**
     * The client connection.
     */
    protected final C connection;

    /**
     * The owner of this connection.
     */
    protected volatile O owner;

    /**
     * The client account.
     */
    protected volatile A account;

    /**
     * The crypt.
     */
    protected volatile T crypt;

    /**
     * The flag of closing this connection.
     */
    protected volatile boolean closed;

    public AbstractClient(@NotNull final C connection, @NotNull final T crypt) {
        this.connection = connection;
        this.crypt = crypt;
    }

    @Override
    public void close() {

        final C connection = getConnection();
        connection.close();

        setClosed(true);
    }

    @Override
    public void decrypt(@NotNull final ByteBuffer data, final int offset, final int length) {
        crypt.decrypt(data.array(), offset, length);
    }

    @Override
    public void encrypt(@NotNull final ByteBuffer data, final int offset, final int length) {
        crypt.encrypt(data.array(), offset, length);
    }

    /**
     * @param packet the packet.
     */
    protected abstract void execute(@NotNull RP packet);

    @Nullable
    @Override
    public final A getAccount() {
        return account;
    }

    @Override
    public final void setAccount(@Nullable final A account) {
        this.account = account;
    }

    @NotNull
    @Override
    public final C getConnection() {
        return connection;
    }

    @Nullable
    @Override
    public final O getOwner() {
        return owner;
    }

    @Override
    public final void setOwner(@Nullable final O owner) {
        this.owner = owner;
    }

    /**
     * @return true if this client is closed.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @param closed true if this client is closed.
     */
    protected void setClosed(final boolean closed) {
        this.closed = closed;
    }

    @Override
    public final boolean isConnected() {
        return !isClosed() && !connection.isClosed();
    }

    @Override
    public final void readPacket(@NotNull final RP packet, @NotNull final ByteBuffer buffer) {
        packet.setOwner(this);
        packet.setBuffer(buffer);
        try {
            if (packet.read()) execute(packet);
        } finally {
            packet.setBuffer(null);
        }
    }

    @Override
    public final void sendPacket(@NotNull final SendablePacket packet) {
        if (isClosed()) return;

        final C connection = getConnection();
        connection.sendPacket(packet);
    }

    @Override
    public void successfulConnection() {
        LOGGER.info(this, getHostAddress() + " successful connection.");
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
