package com.ss.rlib.common.network.client.server.impl;

import com.ss.rlib.common.network.NetworkCrypt;
import com.ss.rlib.common.network.client.server.Server;
import com.ss.rlib.common.network.client.server.ServerConnection;
import com.ss.rlib.common.network.impl.AbstractConnectionOwner;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of {@link Server}.
 *
 * @author JavaSaBr
 */
public class DefaultServer extends AbstractConnectionOwner implements Server {

    public DefaultServer(@NotNull final ServerConnection connection) {
        super(connection, NetworkCrypt.NULL);
    }

    public DefaultServer(@NotNull final ServerConnection connection, @NotNull final NetworkCrypt crypt) {
        super(connection, crypt);
    }

    @Override
    public @NotNull ServerConnection getConnection() {
        return (ServerConnection) super.getConnection();
    }
}
