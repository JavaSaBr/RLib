package rlib.network.client.server.impl;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.network.NetworkCrypt;
import rlib.network.client.server.Server;
import rlib.network.client.server.ServerConnection;
import rlib.network.packet.ReadablePacket;
import rlib.network.packet.SendablePacket;

/**
 * The base implementation of a server.
 *
 * @author JavaSaBr
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServer<C extends ServerConnection, T extends NetworkCrypt, RP extends ReadablePacket,
        SP extends SendablePacket> implements Server<C, RP, SP> {

    protected static final Logger LOGGER = LoggerManager.getLogger(Server.class);

    /**
     * The connection.
     */
    @NotNull
    protected final C connection;

    /**
     * The crypt.
     */
    @NotNull
    protected final T crypt;

    /**
     * The flag of closing connection.
     */
    protected volatile boolean closed;

    protected AbstractServer(@NotNull final C connection, @NotNull final T crypt) {
        this.connection = connection;
        this.crypt = crypt;
    }

    @Override
    public void close() {
        final C connection = getConnection();
        connection.close();
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
     * Execue a packet.
     *
     * @param packet the packet.
     */
    protected abstract void execute(RP packet);

    @NotNull
    @Override
    public C getConnection() {
        return connection;
    }

    /**
     * @return true of this connection is closed.
     */
    public boolean isClosed() {
        return closed;
    }

    @Override
    public boolean isConnected() {
        return !isClosed() && !connection.isClosed();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readPacket(@NotNull final RP packet, @NotNull final ByteBuffer buffer) {

        packet.setOwner(this);
        packet.setBuffer(buffer);
        boolean needExecute = packet.read();
        packet.setBuffer(null);

        if (needExecute) {
            execute(packet);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendPacket(@NotNull final SP packet) {
        if (isClosed()) return;

        final C connection = getConnection();

        if (connection.isClosed()) {
            LOGGER.warning(this, new Exception("not found connection"));
            return;
        }

        connection.sendPacket(packet);
    }

    @Override
    public String toString() {
        return "AbstractServer{" +
                ", crypt=" + crypt +
                ", closed=" + closed +
                '}';
    }
}
