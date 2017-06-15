package com.ss.rlib.network.impl;

import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.network.ConnectionOwner;
import com.ss.rlib.network.NetworkCrypt;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.SendablePacket;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.network.AsyncConnection;

import java.nio.ByteBuffer;

/**
 * The base implementation of a connection owner.
 *
 * @author JavaSaBr
 */
public abstract class AbstractConnectionOwner implements ConnectionOwner {

    /**
     * The constant LOGGER.
     */
    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(Server.class);

    /**
     * The connection.
     */
    @NotNull
    protected final AsyncConnection connection;

    /**
     * The crypt.
     */
    @NotNull
    protected final NetworkCrypt crypt;

    /**
     * The flag of closing connection.
     */
    protected volatile boolean closed;

    /**
     * Instantiates a new Abstract connection owner.
     *
     * @param connection the connection
     * @param crypt      the crypt
     */
    protected AbstractConnectionOwner(@NotNull final AsyncConnection connection, @NotNull final NetworkCrypt crypt) {
        this.connection = connection;
        this.crypt = crypt;
    }

    @Override
    public void close() {
        getConnection().close();
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
     * Execute a packet.
     *
     * @param packet the packet.
     */
    protected abstract void execute(@NotNull ReadablePacket packet);

    @NotNull
    @Override
    public AsyncConnection getConnection() {
        return connection;
    }

    /**
     * Is closed boolean.
     *
     * @return true of this connection is closed.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Sets closed.
     *
     * @param closed true if this client is closed.
     */
    protected void setClosed(final boolean closed) {
        this.closed = closed;
    }

    @Override
    public boolean isConnected() {
        return !isClosed() && !connection.isClosed();
    }

    @Override
    public void readPacket(@NotNull final ReadablePacket packet, @NotNull final ByteBuffer buffer) {
        packet.setOwner(this);

        if (packet.read(buffer)) {
            execute(packet);
        }
    }

    @Override
    public void sendPacket(@NotNull final SendablePacket packet) {
        if (isClosed()) return;

        final AsyncConnection connection = getConnection();

        if (connection.isClosed()) {
            LOGGER.warning(this, new Exception("not found connection"));
            return;
        }

        connection.sendPacket(packet);
    }

    @Override
    public String toString() {
        return "AbstractConnectionOwner{" + "connection=" + connection + ", crypt=" + crypt + ", closed=" + closed +
                '}';
    }
}
