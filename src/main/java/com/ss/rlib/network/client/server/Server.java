package com.ss.rlib.network.client.server;

import com.ss.rlib.network.ConnectionOwner;
import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a server.
 *
 * @author JavaSaBr
 */
public interface Server extends ConnectionOwner {

    @Override
    @NotNull ServerConnection getConnection();
}
