package com.ss.rlib.network.impl;

import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
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

    protected final AsyncConnection connection;
    protected final NetworkCrypt crypt;
    protected final AtomicBoolean destroyed;

    protected AbstractConnectionOwner(@NotNull AsyncConnection connection, @NotNull NetworkCrypt crypt) {
        this.connection = connection;
        this.crypt = crypt;
        this.destroyed = new AtomicBoolean();
    }

    /**
     * The process of destroying.
     */
    protected void doDestroy() {
        getConnection().close();
    }

    @Override
    public @NotNull AsyncConnection getConnection() {
        return connection;
    }

    public boolean isDestroyed() {
        return destroyed.get();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "connection=" +
                connection + ", crypt=" + crypt + ", destroyed=" +
                destroyed + '}';
    }
}
