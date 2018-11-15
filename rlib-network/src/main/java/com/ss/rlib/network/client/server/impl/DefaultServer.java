package com.ss.rlib.network.client.server.impl;

import com.ss.rlib.network.NetworkCrypt;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.client.server.ServerConnection;
import com.ss.rlib.network.impl.AbstractConnectionOwner;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of {@link Server}.
 *
 * @author JavaSaBr
 */
public class DefaultServer extends AbstractConnectionOwner implements Server {

    public DefaultServer(@NotNull ServerConnection connection) {
        super(connection, NetworkCrypt.NULL);
    }

    public DefaultServer(@NotNull ServerConnection connection, @NotNull NetworkCrypt crypt) {
        super(connection, crypt);
    }

    @Override
    public @NotNull ServerConnection getConnection() {
        return (ServerConnection) super.getConnection();
    }
}
