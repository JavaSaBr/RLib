package com.ss.rlib.network.server.client;

import com.ss.rlib.network.AsyncConnection;
import com.ss.rlib.network.server.ServerNetwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface to implement a client connection.
 *
 * @author JavaSaBr
 */
public interface ClientConnection extends AsyncConnection {

    @Override
    @Nullable Client getOwner();

    @Override
    @NotNull ServerNetwork getNetwork();
}
