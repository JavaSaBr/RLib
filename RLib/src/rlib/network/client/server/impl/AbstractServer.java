package rlib.network.client.server.impl;

import org.jetbrains.annotations.NotNull;
import rlib.network.AsyncConnection;
import rlib.network.NetworkCrypt;
import rlib.network.client.server.Server;
import rlib.network.impl.AbstractConnectionOwner;

/**
 * The base implementation of a server.
 *
 * @author JavaSaBr
 */
public abstract class AbstractServer extends AbstractConnectionOwner implements Server {

    protected AbstractServer(@NotNull final AsyncConnection connection, @NotNull final NetworkCrypt crypt) {
        super(connection, crypt);
    }
}
