package com.ss.rlib.network.client.server.impl;

import com.ss.rlib.network.NetworkCrypt;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.network.AsyncConnection;
import com.ss.rlib.network.client.server.Server;
import com.ss.rlib.network.impl.AbstractConnectionOwner;

/**
 * The base implementation of a server.
 *
 * @author JavaSaBr
 */
public abstract class AbstractServer extends AbstractConnectionOwner implements Server {

    /**
     * Instantiates a new Abstract server.
     *
     * @param connection the connection
     * @param crypt      the crypt
     */
    protected AbstractServer(@NotNull final AsyncConnection connection, @NotNull final NetworkCrypt crypt) {
        super(connection, crypt);
    }
}
