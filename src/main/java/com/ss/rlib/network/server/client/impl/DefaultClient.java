package com.ss.rlib.network.server.client.impl;

import com.ss.rlib.network.NetworkCrypt;
import com.ss.rlib.network.impl.AbstractConnectionOwner;
import com.ss.rlib.network.server.ServerNetwork;
import com.ss.rlib.network.server.client.Client;
import com.ss.rlib.network.server.client.ClientConnection;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of {@link Client}.
 *
 * @author JavaSaBr
 */
public class DefaultClient extends AbstractConnectionOwner implements Client {

    public DefaultClient(@NotNull final ClientConnection connection) {
        super(connection, NetworkCrypt.NULL);
    }

    public DefaultClient(@NotNull final ClientConnection connection, @NotNull final NetworkCrypt crypt) {
        super(connection, crypt);
    }

    @Override
    public @NotNull ClientConnection getConnection() {
        return (ClientConnection) super.getConnection();
    }

    @Override
    public void notifyConnected() {
        LOGGER.info(this, "successful connection.");
    }

    @Override
    protected void doDestroy() {
        final ServerNetwork serverNetwork = getConnection().getNetwork();
        super.doDestroy();
        serverNetwork.onDestroyed(this);
    }
}