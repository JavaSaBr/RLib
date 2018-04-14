package com.ss.rlib.common.network.impl;

import com.ss.rlib.common.network.AsyncConnection;
import com.ss.rlib.common.network.client.server.Server;
import com.ss.rlib.common.network.packet.ReadablePacket;
import com.ss.rlib.common.network.packet.WritablePacket;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.common.network.AsyncConnection;
import com.ss.rlib.common.network.ConnectionOwner;
import com.ss.rlib.common.network.NetworkCrypt;
import com.ss.rlib.common.network.client.server.Server;
import com.ss.rlib.common.network.packet.ReadablePacket;
import com.ss.rlib.common.network.packet.WritablePacket;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.StampedLock;

/**
 * The base implementation of {@link ConnectionOwner}.
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
     * The lock.
     */
    @NotNull
    protected final StampedLock lock;

    /**
     * The flag of destroying this owner.
     */
    protected volatile boolean destroyed;

    protected AbstractConnectionOwner(@NotNull final AsyncConnection connection, @NotNull final NetworkCrypt crypt) {
        this.connection = connection;
        this.crypt = crypt;
        this.lock = new StampedLock();
    }

    @Override
    public void destroy() {
        if (isDestroyed()) return;
        final long stamp = lock.writeLock();
        try {
            if (isDestroyed()) return;
            doDestroy();
            setDestroyed(true);
        } finally {
            lock.unlockWrite(stamp);
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
        return destroyed;
    }

    protected void setDestroyed(final boolean destroyed) {
        this.destroyed = destroyed;
    }

    @Override
    public boolean isReady() {
        return !isDestroyed() && !connection.isClosed();
    }

    @Override
    public void readPacket(@NotNull final ReadablePacket packet, @NotNull final ByteBuffer buffer) {
        packet.read(this, buffer);
    }

    @Override
    public void sendPacket(@NotNull final WritablePacket packet) {
        packet.notifyAddedToSend();
        connection.sendPacket(packet);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "connection=" + connection + ", crypt=" + crypt + ", destroyed=" +
                destroyed + '}';
    }
}
