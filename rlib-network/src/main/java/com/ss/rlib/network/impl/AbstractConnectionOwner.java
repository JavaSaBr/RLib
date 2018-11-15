package com.ss.rlib.network.impl;

import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.network.AsyncConnection;
import com.ss.rlib.network.ConnectionOwner;
import com.ss.rlib.network.NetworkCrypt;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.packet.ReadablePacket;
import com.ss.rlib.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The base implementation of {@link ConnectionOwner}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractConnectionOwner implements ConnectionOwner {

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
     * The flag of destroying this owner.
     */
    @NotNull
    protected final AtomicBoolean destroyed;

    protected AbstractConnectionOwner(@NotNull AsyncConnection connection, @NotNull NetworkCrypt crypt) {
        this.connection = connection;
        this.crypt = crypt;
        this.destroyed = new AtomicBoolean();
    }

    @Override
    public void destroy() {
        if (destroyed.compareAndSet(false, true)) {
            doDestroy();
        }
    }

    /**
     * The process of destroying.
     */
    protected void doDestroy() {
        getConnection().close();
    }

    @Override
    public @NotNull NetworkCrypt getCrypt() {
        return crypt;
    }

    @Override
    public @NotNull AsyncConnection getConnection() {
        return connection;
    }

    public boolean isDestroyed() {
        return destroyed.get();
    }

    @Override
    public boolean isReady() {
        return !isDestroyed() && !connection.isClosed();
    }

    @Override
    public void readPacket(@NotNull ReadablePacket packet, @NotNull ByteBuffer buffer) {
        packet.read(this, buffer);
    }

    @Override
    public void sendPacket(@NotNull WritablePacket packet) {
        packet.notifyAddedToSend();
        connection.sendPacket(packet);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "connection=" +
                connection + ", crypt=" + crypt + ", destroyed=" +
                destroyed + '}';
    }
}
