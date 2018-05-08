package com.ss.rlib.common.network.server.client.impl;

import com.ss.rlib.common.network.NetworkCrypt;
import com.ss.rlib.common.network.impl.AbstractConnectionOwner;
import com.ss.rlib.common.network.server.ServerNetwork;
import com.ss.rlib.common.network.server.client.Client;
import com.ss.rlib.common.network.server.client.ClientConnection;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of {@link Client}.
 *
 * @author JavaSaBr
 */
public class DefaultClient extends AbstractConnectionOwner implements Client {

    public DefaultClient(@NotNull ClientConnection connection) {
        super(connection, NetworkCrypt.NULL);
    }

    public DefaultClient(@NotNull ClientConnection connection, @NotNull NetworkCrypt crypt) {
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

        ServerNetwork serverNetwork = getConnection()
                .getNetwork();

        super.doDestroy();

        serverNetwork.onDestroyed(this);
    }
}
