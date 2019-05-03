package com.ss.rlib.network.server.client;

import com.ss.rlib.network.ConnectionOwner;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a client.
 *
 * @author JavaSaBr
 */
public interface Client extends ConnectionOwner {

    @Override
    @NotNull ClientConnection getConnection();
}
